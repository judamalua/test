
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
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	@Autowired
	public MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	public CurriculumService			curriculumService;


	@Test
	public void testCreate() {
		super.authenticate("ranger1");
		final MiscellaneousRecord r = this.miscellaneousRecordService.create();
		Assert.isNull(r.getTitle());
		Assert.isNull(r.getAttachment());
		Assert.isNull(r.getCommentaries());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final MiscellaneousRecord r = this.miscellaneousRecordService.create();
		r.setTitle("Titulo diploma");
		r.setAttachment("http://www.link.com");
		r.setCommentaries("Comentario");
		final MiscellaneousRecord saved = this.miscellaneousRecordService.save(r);
		Assert.isTrue(this.miscellaneousRecordService.findAll().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final MiscellaneousRecord r = (MiscellaneousRecord) this.miscellaneousRecordService.findAll().toArray()[0];
		Assert.notNull(r);
	}
	@Test
	public void testFindAll() {
		final Collection<MiscellaneousRecord> records = this.miscellaneousRecordService.findAll();
		Assert.notNull(records);

	}

	@Test
	public void testDelete() {
		super.authenticate("ranger1");
		final MiscellaneousRecord r = (MiscellaneousRecord) this.miscellaneousRecordService.findAll().toArray()[0];
		final Curriculum curr = this.curriculumService.getCurriculumWithMiscellaneous(r);
		this.miscellaneousRecordService.delete(r);
		Assert.isNull(this.miscellaneousRecordService.findOne(r.getId()));
		Assert.isTrue(!this.curriculumService.findOne(curr.getId()).getMiscellaneousRecords().contains(r));
		super.unauthenticate();
	}
}
