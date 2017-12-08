
package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import security.LoginService;
import security.UserAccount;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@Service
@Transactional
public class CurriculumService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CurriculumRepository	curriculumRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private PersonalRecordService	personalRecordService;

	@Autowired
	private TripService				tripService;

	@Autowired
	private RangerService			rangerService;


	// Simple CRUD methods --------------------------------------------------

	public Curriculum create() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.RANGER));
		String ticker;
		final Ranger r = (Ranger) this.actorService.findActorByUserAccountId(userAccount.getId());
		final PersonalRecord personalRecord = this.personalRecordService.create();
		Curriculum result;
		ticker = this.generateTicker();

		result = new Curriculum();

		final Collection<ProfessionalRecord> pr = new HashSet<>();
		final Collection<EducationRecord> edr = new HashSet<>();
		final Collection<EndorserRecord> enr = new HashSet<>();
		final Collection<MiscellaneousRecord> mr = new HashSet<>();

		result.setEducationRecords(edr);
		result.setEndorserRecords(enr);
		result.setMiscellaneousRecords(mr);
		result.setProfessionalRecords(pr);
		result.setRanger(r);
		result.setTicker(ticker);

		result.setPersonalRecord(personalRecord);

		return result;
	}
	public Collection<Curriculum> findAll() {

		Collection<Curriculum> result;

		Assert.notNull(this.curriculumRepository);
		result = this.curriculumRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Curriculum findOne(final int curriculumId) {

		Curriculum result;

		result = this.curriculumRepository.findOne(curriculumId);

		return result;

	}

	public Curriculum save(final Curriculum curriculum) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.RANGER));
		Assert.isTrue(this.actorService.findActorByUserAccountId(userAccount.getId()).getId() == curriculum.getRanger().getId());

		assert curriculum != null;
		if (curriculum.getVersion() != 0)
			if (!this.curriculumRepository.findOne(curriculum.getId()).getRanger().equals(curriculum.getRanger()))
				if (curriculum.getRanger() != null) {
					final Ranger ranger = curriculum.getRanger();
					ranger.setCurriculum(curriculum);
					this.rangerService.save(ranger);
				}

		Curriculum result;

		result = this.curriculumRepository.save(curriculum);
		Assert.isTrue(!result.getPersonalRecord().equals(null));

		return result;

	}

	public void delete(final Curriculum curriculum) {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		//		Assert.isTrue(userAccount.getAuthorities().contains(Authority.RANGER));
		//		Assert.isTrue(this.actorService.findActorByUserAccountId(userAccount.getId()).getId() == curriculum.getRanger().getId());

		if (curriculum.getRanger() != null) {
			curriculum.getRanger().setCurriculum(null);
			this.rangerService.save(curriculum.getRanger());
		}
		Assert.notNull(curriculum);
		Assert.isTrue(curriculum.getId() != 0);

		//Assert.isNull(this.curriculumRepository.findOne(curriculum.getId()));

		this.curriculumRepository.delete(curriculum);

	}

	// Other business methods --------------------------------------------------

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

		res = year + "" + month + "" + day + "-";

		for (int i = 0; i < 4; i++)
			res += alphabet.charAt(random.nextInt(alphabet.length()));

		if (this.getAllTickers().contains(res))
			res = this.generateTicker();

		return res;
	}

	public Collection<String> getCurriculumTickers() {
		return this.curriculumRepository.getCurriculumTickers();
	}

	private Collection<String> getAllTickers() {
		final Collection<String> curriculumTickers;
		final Collection<String> tripTickers;
		Collection<String> allTickers;

		allTickers = new HashSet<String>();
		curriculumTickers = this.getCurriculumTickers();
		tripTickers = this.tripService.getTripTickers();

		allTickers.addAll(curriculumTickers);
		allTickers.addAll(tripTickers);

		return allTickers;

	}

	public Curriculum getCurriculumWithEndorser(final EndorserRecord record) {
		return this.curriculumRepository.getCurriculumWithEndorser(record.getId());
	}
	//NEW
	public Curriculum findCurriculumByRangerID() {

		final int id = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId();
		Curriculum result;

		result = this.curriculumRepository.getCurriculumByRangerID(id);

		return result;

	}
	public Curriculum getCurriculumWithEducation(final EducationRecord record) {
		return this.curriculumRepository.getCurriculumWithEducation(record.getId());
	}

	public Curriculum getCurriculumWithMiscellaneous(final MiscellaneousRecord record) {
		return this.curriculumRepository.getCurriculumWithMiscellaneous(record.getId());
	}

	public Curriculum getCurriculumWithProfessional(final ProfessionalRecord record) {
		return this.curriculumRepository.getCurriculumWithProfessional(record.getId());
	}
}
