
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
		final EducationRecord r = this.educationRecordService.create();
		Assert.isNull(r.getDiplomaTitle());
		Assert.isNull(r.getStudyingPeriodEnd());
		Assert.isNull(r.getStudyingPeriodStart());
		Assert.isNull(r.getInstitution());
		Assert.isNull(r.getAttachment());
		Assert.isNull(r.getCommentaries());
	}

	@Test
	public void testSave() {
		final EducationRecord r = this.educationRecordService.create();
		r.setDiplomaTitle("Titulo diploma");
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fin = null;
		Date inicio = null;
		try {
			fin = sdf.parse("21/12/2015");
			inicio = sdf.parse("21/12/2012");
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		r.setStudyingPeriodStart(inicio);
		r.setStudyingPeriodEnd(fin);
		r.setInstitution("Institucion");
		r.setAttachment("http://www.link.com");
		r.setCommentaries("Comentario");
		final EducationRecord saved = this.educationRecordService.save(r);
		Assert.isTrue(this.educationRecordService.findAll().contains(saved));
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
