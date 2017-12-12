
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TripRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.AuditRecord;
import domain.Category;
import domain.Configuration;
import domain.Manager;
import domain.Note;
import domain.Sponsorship;
import domain.Stage;
import domain.Story;
import domain.SurvivalClass;
import domain.Tag;
import domain.Trip;

@Service
@Transactional
public class TripService {

	// Managed repository --------------------------------------------------
	@Autowired
	private TripRepository			tripRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;
	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private StageService			stageService;
	@Autowired
	private ManagerService			managerService;
	@Autowired
	private TagService				tagService;


	// Simple CRUD methods --------------------------------------------------

	public Trip create() {
		Trip result;
		String ticker;
		final Collection<Tag> tags = new HashSet<Tag>();
		final Collection<Application> applications = new HashSet<Application>();
		final Collection<AuditRecord> auditRecords = new HashSet<AuditRecord>();
		final Collection<Manager> managers = new HashSet<Manager>();
		final Collection<Note> notes = new HashSet<Note>();
		final Collection<Sponsorship> sponsorships = new HashSet<Sponsorship>();
		final Collection<Stage> stages = new HashSet<Stage>();
		final Collection<SurvivalClass> survivalClasses = new HashSet<SurvivalClass>();
		final Collection<Story> stories = new HashSet<Story>();

		ticker = this.generateTicker();

		result = new Trip();
		// When we create the object, we initialize its tags to an empty list.
		result.setTags(tags);
		result.setTicker(ticker);
		result.setApplications(applications);
		result.setAuditRecords(auditRecords);
		result.setManagers(managers);
		result.setNotes(notes);
		result.setSponsorships(sponsorships);
		result.setStages(stages);
		result.setSurvivalClasses(survivalClasses);
		result.setStories(stories);
		result.setTicker(this.generateTicker());
		result.setPrice(0.);

		return result;
	}
	public Page<Trip> findAll(final Pageable pageable) {

		Page<Trip> result;

		result = this.tripRepository.findAll(pageable);
		Assert.notNull(result.getContent());

		return result;
	}

	public Collection<Trip> findAll() {

		Collection<Trip> result;

		result = this.tripRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Page<Trip> findPublicatedTrips(final Pageable pageable) {

		Page<Trip> result;

		result = this.tripRepository.findPublicatedTrips(pageable);
		Assert.notNull(result);

		return result;
	}

	public Trip findOne(final int tripId) {

		Assert.isTrue(tripId != 0);

		Trip result;

		result = this.tripRepository.findOne(tripId);
		Assert.notNull(result);

		return result;
	}

	public Trip save(final Trip trip) {

		Assert.notNull(trip);
		final Authority auth = new Authority();
		auth.setAuthority(Authority.MANAGER);
		//		if (LoginService.getPrincipal().getAuthorities().contains(auth) && trip.getId() != 0)
		//			Assert.isTrue(trip.getPublicationDate().after(new Date()));
		Assert.isTrue(trip.getStartDate().before(trip.getEndDate()));

		if (trip.getId() == 0) {
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			Assert.isTrue(trip.getStartDate().after(trip.getPublicationDate()));
			Assert.isTrue(trip.getEndDate().after(trip.getStartDate()));
		}

		Collection<Stage> stages, savedStages;
		Stage savedStage;
		double price = 0., vat;
		final Configuration configuration;
		final Collection<Manager> managers;
		final Collection<Tag> tags;

		if (trip.getPublicationDate().before(new Date()))
			Assert.isTrue(trip.getCancelReason() == null || trip.getCancelReason() == "");
		if (trip.getCancelReason() != null && trip.getCancelReason() != "")
			Assert.isTrue(trip.getPublicationDate().after(new Date()) && trip.getStartDate().after(new Date()));

		Trip result;
		// Requirement 14.2: Only legal texts that are saved in final mode 
		// can be referenced by trips
		if (trip.getLegalText() != null)
			Assert.isTrue(trip.getLegalText().getFinalMode());

		stages = trip.getStages();
		savedStages = new HashSet<Stage>();
		stages.remove(null);
		for (final Stage stage : stages) {
			savedStage = this.stageService.save(stage);
			savedStages.add(savedStage);
		}
		trip.setStages(savedStages);

		configuration = this.configurationService.findConfiguration();

		price = this.getPriceStages(trip);
		vat = price * configuration.getVat();
		trip.setPrice(price + vat);

		if (trip.getTags().contains(null))
			trip.getTags().remove(null);

		result = this.tripRepository.save(trip);

		if (trip.getId() != 0) {

			tags = result.getTags();

			for (final Tag t : tags)
				if (!t.getTrips().contains(result)) {
					if (t.getTrips().contains(trip))
						t.getTrips().remove(trip);
					t.getTrips().add(result);
					this.tagService.save(t);
				}

			//managers = this.managerService.findManagerByTrip(trip.getId());

		}
		managers = result.getManagers();
		for (final Manager m : managers)
			if (!m.getTrips().contains(result)) {
				if (m.getTrips().contains(trip))
					m.getTrips().remove(trip);
				m.getTrips().add(result);
				this.managerService.save(m);
			}

		return result;
	}
	public void delete(final Trip trip) {
		Assert.notNull(trip);
		Assert.isTrue(trip.getId() != 0);
		Assert.isTrue(this.tripRepository.exists(trip.getId()));
		Assert.isTrue(trip.getPublicationDate().after(new Date()));

		Collection<Manager> managers;

		managers = trip.getManagers();

		for (final Manager m : managers) {
			m.getTrips().remove(trip);
			this.managerService.save(m);
		}

		this.tripRepository.delete(trip);
	}

	// Other business methods --------------------------------------------------

	public Collection<Trip> findTrips(final String keyword) {

		Collection<Trip> result;

		if (keyword != null && keyword != "")
			result = this.tripRepository.findTrips(keyword);
		else
			result = this.findAll();
		return result;
	}

	public Collection<Trip> findTrips(final Category category) {

		Collection<Trip> result;

		if (category != null && category.getId() != 0)
			result = this.tripRepository.findTripsByCategoryId(category.getId());
		else
			result = this.findAll();

		return result;
	}

	public Collection<Trip> findTripsBySurvivalClass(final SurvivalClass s) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		return this.tripRepository.findTripBySurvivalClass(s.getId());
	}
	public Collection<String> getInfoNotesFromTrips() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		return this.tripRepository.getInfoNotesFromTrips();
	}
	public Collection<String> getInfoAuditsFromTrips() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		return this.tripRepository.getInfoAuditsFromTrips();
	}
	public String getRatioAuditsFromTrips() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		return this.tripRepository.getRatioAuditsFromTrips();
	}

	// Requisito funcional 14.6, query C/1.
	public Collection<String> getApplicationsInfoFromTrips() {
		this.actorService.checkUserLogin();

		return this.tripRepository.getApplicationsInfoFromTrips();
	}

	// Requisito funcional 14.6, query C/3.
	public Collection<String> getPriceInfoFromTrips() {
		this.actorService.checkUserLogin();

		return this.tripRepository.getPriceInfoFromTrips();
	}

	// Requisito funcional 14.6, query C/9.
	public String getRatioTripsCancelled() {
		this.actorService.checkUserLogin();

		return this.tripRepository.getRatioTripsCancelled();
	}

	// Requisito funcional 14.6, query C/10.
	public Collection<Trip> getTripsTenPercentMoreApplications() {
		this.actorService.checkUserLogin();

		return this.tripRepository.getTripsTenPercentMoreApplications();
	}

	// Requisito funcional 14.6, query C/11.
	public Collection<String> getNumberOfReferencesLegalTexts() {
		this.actorService.checkUserLogin();

		return this.tripRepository.getNumberOfReferencesLegalTexts();
	}

	public Collection<String> getTripTickers() {
		return this.tripRepository.getTripTickers();
	}

	public String generateTicker() {
		String res;
		final int year, month, day;
		final String alphabet;
		final LocalDate currDate;
		Random random;

		currDate = new LocalDate();
		year = currDate.getYear() % 100;
		month = currDate.getMonthOfYear();
		day = currDate.getDayOfMonth();
		random = new Random();

		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		res = (year < 10 ? "0" + year : year) + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day) + "-";

		for (int i = 0; i < 4; i++)
			res += alphabet.charAt(random.nextInt(alphabet.length()));

		if (this.getAllTickers().contains(res))
			res = this.generateTicker();

		return res;
	}

	private Collection<String> getAllTickers() {
		final Collection<String> curriculumTickers;
		final Collection<String> tripTickers;
		Collection<String> allTickers;

		allTickers = new HashSet<String>();
		curriculumTickers = this.curriculumService.getCurriculumTickers();
		tripTickers = this.getTripTickers();

		allTickers.addAll(curriculumTickers);
		allTickers.addAll(tripTickers);

		return allTickers;

	}

	/**
	 * 
	 * @param stageId
	 * @return the trip associated to the stage sent by parameters
	 * @author Juanmi
	 */
	public Trip getTripFromStageId(final int stageId) {
		Trip trip;

		trip = this.tripRepository.getTripFromStageId(stageId);
		Assert.notNull(trip);

		return trip;
	}

	private double getPriceStages(final Trip trip) {
		Assert.notNull(trip);

		double result;

		result = 0.;
		for (final Stage stage : trip.getStages())
			result += stage.getPrice();

		return result;
	}

	public Trip getTripFromStory(final int storyId) {

		Trip result;

		result = this.tripRepository.getTripFromStory(storyId);

		return result;
	}
	public Collection<Trip> getAcceptedTripsFromExplorerId(final int explorerId) {
		this.actorService.checkUserLogin();

		return this.tripRepository.getAcceptedTripsFromExplorerId(explorerId);
	}

	//Requirement 44 
	public Trip findTripApplicationSurvival(final SurvivalClass sc) {
		Assert.notNull(sc);

		Trip result;
		result = this.tripRepository.findTripSurvivalApplication(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId(), sc.getId());

		return result;
	}

	//Paginated queries

	public Page<Trip> findTrips(final String keyword, final Pageable pageable) {

		Page<Trip> result;

		if (keyword != null && keyword != "")
			result = this.tripRepository.findTrips(keyword, pageable);
		else
			result = this.findAll(pageable);
		return result;
	}


	String	r	= new LocalDateTime(2000, 1, 1, 10, 10).toDate().toString();


	//Requirement 34
	public Page<Trip> findTripsBySearchParameters(final String q, final Double pricelow, final Double pricehigh, final Date date1, final Date date2, final Pageable pageable) {
		final Page<Trip> t1;

		if (q.equals("") || q == null)
			t1 = this.tripRepository.findTripsBySearchParametersWithoutQ(date1, date2, pricelow, pricehigh, pageable);
		else
			t1 = this.tripRepository.findTripsBySearchParameters(q, date1, date2, pricelow, pricehigh, pageable);
		return t1;
	}
	//	public Collection<Trip> findTripsApplicationExplorer(final int id) {
	//		return this.tripRepository.findTripsApplicationExplorer(id);
	//	}

	public Collection<Trip> findTrips(final Category category, final Pageable pageable) {

		Collection<Trip> result;

		if (category != null && category.getId() != 0)
			result = this.tripRepository.findTripsByCategoryId(category.getId(), pageable).getContent();
		else
			result = this.findAll();

		return result;
	}

	public Collection<Trip> findTripsByCategoryId(final int id) {
		Assert.isTrue(id != 0);
		Collection<Trip> result;

		result = this.tripRepository.findTripsByCategoryId(id);

		Assert.notNull(result);

		return result;

	}

}
