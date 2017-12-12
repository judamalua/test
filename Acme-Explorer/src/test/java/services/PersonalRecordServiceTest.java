
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.PersonalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PersonalRecordServiceTest extends AbstractTest {

	@Autowired
	public PersonalRecordService	personalRecordService;


	@Test
	public void testCreate() {
		super.authenticate("ranger1");
		final PersonalRecord r = this.personalRecordService.create();
		Assert.isNull(r.getEmail());
		Assert.isNull(r.getLinkedInProfileURL());
		Assert.isNull(r.getNameOfCandidate());
		Assert.isNull(r.getPhoneNumber());
		Assert.isNull(r.getPhoto());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final PersonalRecord r = this.personalRecordService.create();
		r.setEmail("cosa@cosa.com");
		r.setLinkedInProfileURL("http://www.link.com");
		r.setNameOfCandidate("Manolo Manolos");
		r.setPhoneNumber("666999666");
		r.setPhoto("http://www.link.com");
		final PersonalRecord saved = this.personalRecordService.save(r);
		Assert.isTrue(this.personalRecordService.findAll().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final PersonalRecord r = (PersonalRecord) this.personalRecordService.findAll().toArray()[0];
		Assert.isTrue(r.getNameOfCandidate().equals("Jhon"));
	}
	@Test
	public void testFindAll() {
		Assert.isTrue(this.personalRecordService.findAll().size() != 0);
	}

}
