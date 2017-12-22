
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.Explorer;
import domain.Manager;
import domain.Ranger;
import domain.Search;
import domain.Stage;
import domain.Story;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TripServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public CategoryService		categoryService;
	@Autowired
	public ManagerService		managerService;
	@Autowired
	public RangerService		rangerService;
	@Autowired
	public StageService			stageService;
	@Autowired
	public StoryService			storyService;
	@Autowired
	public ExplorerService		explorerService;
	@Autowired
	public SurvivalClassService	survivalClassService;
	@Autowired
	public SearchService		searchService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");

		Trip trip;

		trip = this.tripService.create();

		Assert.isTrue(trip.getApplications().size() == 0);
		Assert.isTrue(trip.getAuditRecords().size() == 0);
		Assert.isTrue(trip.getCancelReason() == null);
		Assert.isTrue(trip.getCategory() == null);
		Assert.isTrue(trip.getEndDate() == null);
		Assert.isTrue(trip.getLegalText() == null);
		Assert.isTrue(trip.getManagers().size() == 0);
		Assert.isTrue(trip.getNotes().size() == 0);
		Assert.isTrue(trip.getPrice() == 0.);
		Assert.isTrue(trip.getPublicationDate() == null);
		Assert.isTrue(trip.getRanger() == null);
		Assert.isTrue(trip.getRequirements() == null);
		Assert.isTrue(trip.getRequirements() == null);
		Assert.isTrue(trip.getRanger() == null);
		Assert.isTrue(trip.getRequirements() == null);
		Assert.isTrue(trip.getSponsorships().size() == 0);
		Assert.isTrue(trip.getStages().size() == 0);
		Assert.isTrue(trip.getStartDate() == null);
		Assert.isTrue(trip.getTitle() == null);
		Assert.isTrue(trip.getTicker() != null);
		Assert.isTrue(trip.getSurvivalClasses().size() == 0);

		super.unauthenticate();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSaveTrip() {

		super.authenticate("admin1");

		Trip trip;
		Category category;
		Collection<Manager> managers;
		final Collection<Stage> stages;
		Stage stage;
		Manager manager;
		Ranger ranger;

		stages = new HashSet<>();

		trip = this.tripService.create();
		category = (Category) this.categoryService.findAll().toArray()[0];
		managers = new HashSet<Manager>();
		manager = (Manager) this.managerService.findAll().toArray()[0];
		managers.add(manager);
		ranger = (Ranger) this.rangerService.findAll().toArray()[0];
		stage = this.stageService.create();
		stage.setDescription("Stage 1");
		stage.setPrice(1000.);
		stage.setTitle("Stage 1");

		stages.add(stage);

		trip.setCategory(category);
		trip.setDescription("Increible viaje");
		trip.setEndDate(new Date(2025, 10, 10));
		trip.setManagers(managers);
		trip.setPublicationDate(new Date(System.currentTimeMillis() + 10000000));
		trip.setRanger(ranger);
		trip.setRequirements("Requeriments");
		trip.setStages(stages);
		final Date d = new Date(1990, 10, 10);
		trip.setStartDate(d);
		trip.setTitle("Viaje perfecto");

		trip = this.tripService.save(trip);

		Assert.isTrue(this.tripService.findOne(trip.getId()) != null);
		for (final Stage s : trip.getStages())
			Assert.isTrue(trip.getStages().contains(s));

		super.unauthenticate();
	}
	@Test
	public void testFindAll() {

		final Collection<Trip> trips = this.tripService.findAll();
		Assert.notNull(trips);

	}

	@Test
	public void testFindOne() {
		final Trip trip1 = (Trip) this.tripService.findAll().toArray()[1];
		final int tripId = trip1.getId();

		final Trip trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
	}

	@Test
	public void testDelete() {
		super.authenticate("ranger4");
		final Trip trip = (Trip) this.tripService.findAll().toArray()[4];

		Assert.notNull(trip);

		this.tripService.delete(trip);

		Assert.isTrue(!this.tripService.findAll().contains(trip));

		super.unauthenticate();
	}

	@Test
	public void testFindTripsByKeyword() {
		Collection<Trip> result;

		result = this.tripService.findTrips("El último viaje");

		Assert.isTrue(!result.equals(null));
	}

	@Test
	public void testFindTripsByCategory() {
		Collection<Trip> result;
		final Category category = (Category) this.categoryService.findAll().toArray()[0];

		result = this.tripService.findTrips(category);

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());
	}

	@Test
	public void testGetInfoNotesFromTrip() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getInfoNotesFromTrips();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetInfoAuditsFromTrip() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getInfoAuditsFromTrips();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetRatioAuditsFromTrips() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getRatioAuditsFromTrips();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.equals(""));

		super.unauthenticate();
	}

	@Test
	public void testGetApplicationsInfoFromTrips() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getApplicationsInfoFromTrips();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetPriceInfoFromTrips() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getPriceInfoFromTrips();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetRatioTripsCancelled() {
		super.authenticate("admin1");
		String result;

		result = this.tripService.getRatioTripsCancelled();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.equals(""));

		super.unauthenticate();
	}

	@Test
	public void testGetTripsTenPercentMoreApplications() {
		super.authenticate("admin1");
		Collection<Trip> result;

		result = this.tripService.getTripsTenPercentMoreApplications();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetNumberOfReferencesLegalTexts() {
		super.authenticate("admin1");
		Collection<String> result;

		result = this.tripService.getNumberOfReferencesLegalTexts();

		Assert.isTrue(!result.equals(null));
		Assert.isTrue(!result.isEmpty());

		super.unauthenticate();
	}

	@Test
	public void testGetTripFromStageId() {
		super.authenticate("admin1");
		Assert.notNull(this.tripService.getTripFromStageId(((Stage) this.stageService.findAll().toArray()[0]).getId()));
		super.unauthenticate();
	}
	@Test
	public void testGetTripFromStory() {
		super.authenticate("admin1");
		Assert.notNull(this.tripService.getTripFromStory(((Story) this.storyService.findAll().toArray()[0]).getId()));
		super.unauthenticate();
	}
	@Test
	public void testGetAcceptedTripsFromExplorerId() {
		super.authenticate("admin1");
		Assert.notNull(this.tripService.getAcceptedTripsFromExplorerId(((Explorer) this.explorerService.findAll().toArray()[0]).getId()));
		super.unauthenticate();
	}

	@Test
	public void testFindTripBySearchParameters() {

		super.authenticate("admin1");
		final Search s = this.searchService.create();
		s.setKeyWord("Cheap Trip");
		s.setPriceRangeEnd(2000.0);
		s.setPriceRangeStart(20.0);
		s.setDateRangeStart(new LocalDateTime(2000, 1, 1, 10, 10).toDate());
		s.setDateRangeEnd(new LocalDateTime(2050, 1, 1, 10, 10).toDate());

		final Pageable pageable = new PageRequest(0, 10);
		final Collection<Trip> t = this.tripService.findTripsBySearchParameters("Cheap Trip", pageable).getContent();
		Assert.notNull(t);
		super.unauthenticate();

	}

}
