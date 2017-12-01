
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CurriculumServiceTest extends AbstractTest {

	@Autowired
	public CurriculumService			curriculumService;

	@Autowired
	public ActorService					actorService;

	@Autowired
	public PersonalRecordService		personalRecordService;

	@Autowired
	public EndorserRecordService		endorserRecordService;

	@Autowired
	public MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	public EducationRecordService		educationRecordService;

	@Autowired
	public ProfessionalRecordService	professionalRecordService;


	@Test
	public void testCreate() {
		super.authenticate("ranger1");
		Curriculum c;
		c = this.curriculumService.create();

		Assert.notNull(c);
		Assert.notNull(c.getTicker());

		super.unauthenticate();
	}
	@Test
	public void testFindAll() {
		super.authenticate("ranger1");

		final Collection<Curriculum> cs = this.curriculumService.findAll();

		Assert.notNull(cs);
		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("ranger1");
		final Curriculum c = (Curriculum) this.curriculumService.findAll().toArray()[0];
		final int cId = c.getId();

		final Curriculum cu = this.curriculumService.findOne(cId);
		Assert.notNull(cu);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final Curriculum c = this.curriculumService.create();
		final Ranger r = (Ranger) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		c.setRanger(r);
		c.setPersonalRecord((PersonalRecord) this.personalRecordService.findAll().toArray()[0]);

		final Curriculum savedc = this.curriculumService.save(c);

		Assert.isTrue(!this.curriculumService.findOne(savedc.getId()).equals(null));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("ranger1");

		final Curriculum c = (Curriculum) this.curriculumService.findAll().toArray()[0];

		Assert.notNull(c);
		Assert.notNull(c.getPersonalRecord());

		this.curriculumService.delete(c);

		Assert.isTrue(!this.curriculumService.findAll().contains(c));

		super.unauthenticate();
	}
	@Test
	public void testGetCurriculumWithEndorserRecord() {
		final EndorserRecord er = (EndorserRecord) this.endorserRecordService.findAll().toArray()[0];
		this.curriculumService.getCurriculumWithEndorser(er);
		Assert.notNull(er);

	}
	@Test
	public void testGetCurriculumWithEducationRecord() {
		final EducationRecord er = (EducationRecord) this.educationRecordService.findAll().toArray()[0];
		this.curriculumService.getCurriculumWithEducation(er);
		Assert.notNull(er);

	}
	@Test
	public void testGetCurriculumWithProfesionalRecord() {
		final ProfessionalRecord er = (ProfessionalRecord) this.professionalRecordService.findAll().toArray()[0];
		this.curriculumService.getCurriculumWithProfessional(er);
		Assert.notNull(er);

	}
	@Test
	public void testGetCurriculumWithMiscellaneousRecord() {
		final MiscellaneousRecord er = (MiscellaneousRecord) this.miscellaneousRecordService.findAll().toArray()[0];
		this.curriculumService.getCurriculumWithMiscellaneous(er);
		Assert.notNull(er);

	}

	@Test
	public void testGetCurriculumTickers() {
		final Collection<String> ct = this.curriculumService.getCurriculumTickers();
		Assert.notNull(ct);
	}
}
