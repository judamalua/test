
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
import domain.EndorserRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EndorserRecordServiceTest extends AbstractTest {

	@Autowired
	public EndorserRecordService	endorserRecordService;
	@Autowired
	public CurriculumService		curriculumService;


	@Test
	public void testCreate() {
		super.authenticate("ranger2");
		final EndorserRecord r = this.endorserRecordService.create();
		Assert.isNull(r.getFullName());
		Assert.isNull(r.getPhoneNumber());
		Assert.isNull(r.getEmail());
		Assert.isNull(r.getLinkedInProfileURL());
		Assert.isNull(r.getCommentaries());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger2");
		final EndorserRecord r = this.endorserRecordService.create();
		r.setFullName("Jhon Mac");
		r.setPhoneNumber("6733445588");
		r.setEmail("test@hmail.com");
		r.setLinkedInProfileURL("http://www.link.com");
		r.setCommentaries("Comentario");
		final EndorserRecord saved = this.endorserRecordService.save(r);
		Assert.isTrue(this.endorserRecordService.findAll().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("ranger2");
		final EndorserRecord r = (EndorserRecord) this.endorserRecordService.findAll().toArray()[0];
		Assert.notNull(r);
		super.unauthenticate();
	}
	@Test
	public void testFindAll() {
		super.authenticate("ranger2");
		final Collection<EndorserRecord> records = this.endorserRecordService.findAll();
		Assert.notNull(records);
		super.unauthenticate();

	}

	@Test
	public void testDelete() {
		super.authenticate("ranger2");
		final EndorserRecord r = (EndorserRecord) this.endorserRecordService.findAll().toArray()[1];
		final Curriculum curr = this.curriculumService.getCurriculumWithEndorser(r);
		this.endorserRecordService.delete(r);
		Assert.isNull(this.endorserRecordService.findOne(r.getId()));
		Assert.isTrue(!this.curriculumService.findOne(curr.getId()).getEndorserRecords().contains(r));
		super.unauthenticate();
	}
}
