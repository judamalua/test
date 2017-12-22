
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curriculum;
import domain.EducationRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EducationRecordServiceTest extends AbstractTest {

	@Autowired
	public EducationRecordService	educationRecordService;
	@Autowired
	public CurriculumService		curriculumService;


	@Test
	public void testCreate() {
		super.authenticate("ranger1");

		final EducationRecord r = this.educationRecordService.create();
		Assert.isNull(r.getDiplomaTitle());
		Assert.isNull(r.getStudyingPeriodEnd());
		Assert.isNull(r.getStudyingPeriodStart());
		Assert.isNull(r.getInstitution());
		Assert.isNull(r.getAttachment());
		Assert.isNull(r.getCommentaries());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger1");

		final EducationRecord er = (EducationRecord) this.educationRecordService.findAll().toArray()[0];
		er.setCommentaries("comentarios varios");
		er.setDiplomaTitle("graduado escolar");
		final EducationRecord saved = this.educationRecordService.save(er);
		final int id = saved.getId();
		Assert.notNull(this.educationRecordService.findOne(id));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final EducationRecord r = (EducationRecord) this.educationRecordService.findAll().toArray()[0];
		Assert.notNull(r);
	}
	@Test
	public void testFindAll() {
		final Collection<EducationRecord> records = this.educationRecordService.findAll();
		Assert.notNull(records);
	}

	@Test
	public void testDelete() {
		super.authenticate("ranger1");

		final EducationRecord r = (EducationRecord) this.educationRecordService.findAll().toArray()[0];
		final Curriculum curr = this.curriculumService.getCurriculumWithEducation(r);
		this.educationRecordService.delete(r);
		Assert.isNull(this.educationRecordService.findOne(r.getId()));
		Assert.isTrue(!this.curriculumService.findOne(curr.getId()).getEducationRecords().contains(r));
		super.unauthenticate();
	}
}
