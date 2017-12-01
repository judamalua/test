
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
import domain.AuditRecord;
import domain.Auditor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AuditRecordServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public AuditorService		auditorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public AuditRecordService	auditRecordService;


	@Test
	public void testCreate() {
		super.authenticate("auditor1");
		AuditRecord auditRecord;
		auditRecord = this.auditRecordService.create();

		Assert.notNull(auditRecord);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("auditor1");

		final Collection<AuditRecord> ar = this.auditRecordService.findAll();

		Assert.notEmpty(ar);
		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("auditor1");

		final AuditRecord ar = (AuditRecord) this.auditRecordService.findAll().toArray()[0];
		final int arId = ar.getId();

		final AuditRecord arecord = this.auditRecordService.findOne(arId);
		Assert.notNull(arecord);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("auditor1");
		final AuditRecord ar = (AuditRecord) this.auditRecordService.findAll().toArray()[0];

		ar.setTitle("AuditRecord10");

		final AuditRecord savedAr = this.auditRecordService.save(ar);

		Assert.isTrue(this.auditRecordService.findAll().contains(savedAr));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("auditor1");
		final AuditRecord ar = (AuditRecord) this.auditRecordService.findAll().toArray()[0];
		Assert.notNull(ar);

		this.auditRecordService.delete(ar);

		Assert.isTrue(!this.auditRecordService.findAll().contains(ar));

		super.unauthenticate();
	}

	@Test
	public void testFindRecordsByAuditorId() {
		super.authenticate("auditor1");
		final Collection<AuditRecord> ars = this.auditRecordService.findRecordsByAuditorID();
		Assert.notNull(ars);
		super.unauthenticate();
	}
	@Test
	public void testFindAuditsByManagerId() {
		super.authenticate("manager1");
		final Collection<AuditRecord> ars = this.auditRecordService.findAuditsByManagerID();
		Assert.notNull(ars);
		super.unauthenticate();
	}
	@Test
	public void testFindARecordsByAuditorId() {
		super.authenticate("auditor1");
		final Auditor a = (Auditor) this.auditorService.findAll().toArray()[0];
		final int id = a.getId();
		final Collection<AuditRecord> ars = this.auditRecordService.findARecordsByAuditorID(id);
		Assert.notNull(ars);
		super.unauthenticate();
	}

}
