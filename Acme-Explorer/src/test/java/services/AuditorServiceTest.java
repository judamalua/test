
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
import domain.Auditor;
import domain.Note;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AuditorServiceTest extends AbstractTest {

	@Autowired
	public AuditorService		auditorService;
	@Autowired
	public AuditRecordService	auditRecordService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Auditor a;
		a = this.auditorService.create();

		Assert.notNull(a);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");

		final Collection<Auditor> auditors = this.auditorService.findAll();

		Assert.notNull(auditors);
		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final Auditor auditor1 = (Auditor) this.auditorService.findAll().toArray()[1];
		final int auditorId = auditor1.getId();

		final Auditor auditor = this.auditorService.findOne(auditorId);
		Assert.notNull(auditor);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Auditor auditor = (Auditor) this.auditorService.findAll().toArray()[0];

		auditor.setName("Name");

		final Auditor savedAuditor = this.auditorService.save(auditor);

		Assert.isTrue(this.auditorService.findAll().contains(savedAuditor));
		Assert.isTrue(savedAuditor.getName().equals("Name"));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("admin1");

		final Auditor auditor = (Auditor) this.auditorService.findAll().toArray()[0];

		Assert.notNull(auditor);
		final Collection<Note> notes = auditor.getNotes();

		this.auditorService.delete(auditor);

		if (notes.isEmpty())
			Assert.isTrue(!this.auditorService.findAll().contains(auditor));

		super.unauthenticate();
	}
}
