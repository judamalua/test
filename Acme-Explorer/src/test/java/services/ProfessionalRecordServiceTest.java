
package services;

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
import domain.ProfessionalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ProfessionalRecordServiceTest extends AbstractTest {

	@Autowired
	private ProfessionalRecordService	professionalRecordService;
	@Autowired
	private CurriculumService			curriculumService;


	@Test
	public void testCreate() {
		super.authenticate("ranger1");
		final ProfessionalRecord r = this.professionalRecordService.create();
		Assert.isNull(r.getAttachment());
		Assert.isNull(r.getCommentaries());
		Assert.isNull(r.getCompanyName());
		Assert.isNull(r.getRole());
		Assert.isNull(r.getWorkingPeriodEnd());
		Assert.isNull(r.getWorkingPeriodStart());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final ProfessionalRecord r = this.professionalRecordService.create();
		r.setAttachment("http://www.link.com");
		r.setCommentaries("Comentario");
		r.setCompanyName("AcmeExplorer");
		r.setRole("Jefe Supremo de todo el universo");
		r.setWorkingPeriodEnd(new Date());
		r.setWorkingPeriodStart(new Date(System.currentTimeMillis() - 5000));
		final ProfessionalRecord saved = this.professionalRecordService.save(r);
		Assert.isTrue(this.professionalRecordService.findAll().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final ProfessionalRecord r = (ProfessionalRecord) this.professionalRecordService.findAll().toArray()[0];
		Assert.isTrue(r.getAttachment().equals("http://www.proffesionalRecord1.com"));
	}
	@Test
	public void testFindAll() {
		Assert.isTrue(this.professionalRecordService.findAll().size() != 0);
	}

	@Test
	public void testDelete() {
		super.authenticate("ranger1");

		final ProfessionalRecord r = (ProfessionalRecord) this.professionalRecordService.findAll().toArray()[0];
		final Curriculum curr = this.curriculumService.getCurriculumWithProfessional(r);
		this.professionalRecordService.delete(r);
		Assert.isNull(this.professionalRecordService.findOne(r.getId()));
		Assert.isTrue(!this.curriculumService.findOne(curr.getId()).getProfessionalRecords().contains(r));
		super.unauthenticate();
	}
}
