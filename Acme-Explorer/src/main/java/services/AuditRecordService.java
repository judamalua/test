
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AuditRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.AuditRecord;
import domain.Auditor;
import domain.Trip;

@Service
@Transactional
public class AuditRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AuditRecordRepository	auditRecordRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private TripService				tripService;
	@Autowired
	private AuditorService			auditorService;
	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------

	public AuditRecord create() {
		this.checkUserLogin();
		final Collection<String> at = new HashSet<String>();

		AuditRecord result;

		result = new AuditRecord();
		result.setAttachments(at);
		result.setMomentWhenCarriedOut(new Date(System.currentTimeMillis() - 1000));
		result.setIsFinalMode(false);
		return result;
	}
	public Collection<AuditRecord> findAll() {
		this.checkUserLogin();

		Collection<AuditRecord> result;

		Assert.notNull(this.auditRecordRepository);
		result = this.auditRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public AuditRecord findOne(final int auditId) {
		this.checkUserLogin();

		AuditRecord result;

		result = this.auditRecordRepository.findOne(auditId);

		return result;

	}

	public AuditRecord save(final AuditRecord audit) {
		this.checkUserLogin();

		assert audit != null;
		//Requirement 33
		if (audit.getVersion() != 0)
			if (audit.getIsFinalMode())
				Assert.isTrue(!this.auditRecordRepository.findOne(audit.getId()).getIsFinalMode());

		//Requirement 33
		AuditRecord result;
		Trip trip;
		Auditor auditor;

		trip = audit.getTrip();
		auditor = audit.getAuditor();

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Auditor) {
			this.actorService.checkSpamWords(audit.getTitle());
			this.actorService.checkSpamWords(audit.getDescription());
			this.actorService.checkSpamWords(audit.getAttachments());
		}

		audit.setMomentWhenCarriedOut(new Date(System.currentTimeMillis() - 1000));
		result = this.auditRecordRepository.save(audit);

		if (auditor.getAuditRecords().contains(audit))
			auditor.getAuditRecords().remove(audit);
		if (trip.getAuditRecords().contains(audit))
			trip.getAuditRecords().remove(audit);

		auditor.getAuditRecords().add(result);
		trip.getAuditRecords().add(result);

		this.auditorService.save(auditor);
		this.tripService.save(trip);

		return result;

	}
	public void delete(final AuditRecord audit) {
		this.checkUserLogin();

		assert audit != null;
		Auditor auditor;
		Trip trip;

		//Requirement 33
		Assert.isTrue(!audit.getIsFinalMode());

		Assert.isTrue(this.auditRecordRepository.exists(audit.getId()));

		auditor = audit.getAuditor();
		auditor.getAuditRecords().remove(audit);
		this.auditorService.save(auditor);

		trip = audit.getTrip();
		trip.getAuditRecords().remove(audit);
		this.tripService.save(trip);

		this.auditRecordRepository.delete(audit);

	}
	// Other Business Methods --------------------------------------------------

	//Requirement 33: And Auditor can list his Trips;
	public Collection<AuditRecord> findRecordsByAuditorID() {
		this.checkUserLogin();
		final int id = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId();
		return this.auditRecordRepository.findAuditRecordsByAuditorID(id);
	}

	//Requirement 32: A manager can list the auditRecords of his trips
	public Collection<AuditRecord> findAuditsByManagerID() {
		this.checkUserLogin();

		final int id = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId();
		return this.auditRecordRepository.findAuditRecordsByManagerId(id);
	}
	public Collection<AuditRecord> findARecordsByAuditorID(final int id) {
		return this.auditRecordRepository.findAuditRecordsByManagerId(id);

	}

	//Other Methods

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

	public Collection<AuditRecord> findAllAuditsByManagerID() {
		this.checkUserLogin();

		final int id = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId();
		return this.auditRecordRepository.findAuditRecordsByManagerIdWithNoFinalMode(id);
	}
}
