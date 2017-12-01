
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AuditorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.AuditRecord;
import domain.Auditor;
import domain.MessageFolder;
import domain.Note;
import domain.SocialIdentity;

@Service
@Transactional
public class AuditorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AuditorRepository		auditorRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private AuditRecordService		auditRecordService;


	// Simple CRUD methods --------------------------------------------------

	public Auditor create() {
		this.checkUserLogin();
		Auditor result;

		result = new Auditor();

		final Collection<AuditRecord> init = new ArrayList<>();
		final Collection<Note> notes = new ArrayList<>();

		result.setAuditRecords(init);
		result.setNotes(notes);

		final MessageFolder inbox = this.messageFolderService.create();
		inbox.setIsDefault(true);
		inbox.setMessageFolderFather(null);
		inbox.setName("in box");
		final MessageFolder outbox = this.messageFolderService.create();
		outbox.setIsDefault(true);
		outbox.setMessageFolderFather(null);
		outbox.setName("out box");
		final MessageFolder notificationbox = this.messageFolderService.create();
		notificationbox.setIsDefault(true);
		notificationbox.setMessageFolderFather(null);
		notificationbox.setName("notification box");
		final MessageFolder trashbox = this.messageFolderService.create();
		trashbox.setIsDefault(true);
		trashbox.setMessageFolderFather(null);
		trashbox.setName("trash box");
		final MessageFolder spambox = this.messageFolderService.create();
		spambox.setIsDefault(true);
		spambox.setMessageFolderFather(null);
		spambox.setName("spam box");

		final Collection<MessageFolder> messageFolders = new ArrayList<>();
		messageFolders.add(inbox);
		messageFolders.add(outbox);
		messageFolders.add(trashbox);
		messageFolders.add(spambox);
		messageFolders.add(notificationbox);

		final Collection<MessageFolder> savedMessageFolders = new ArrayList<MessageFolder>();

		for (final MessageFolder mf : messageFolders)
			savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

		result.setMessageFolders(savedMessageFolders);

		result.setSocialIdentities(new ArrayList<SocialIdentity>());

		result.setIsBanned(false);
		result.setSuspicious(false);

		return result;
	}

	public Collection<Auditor> findAll() {
		this.checkUserLogin();

		Collection<Auditor> result;

		Assert.notNull(this.auditorRepository);
		result = this.auditorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Auditor findOne(final int auditorId) {
		this.checkUserLogin();
		Auditor result;

		result = this.auditorRepository.findOne(auditorId);

		return result;

	}

	public Auditor save(final Auditor auditor) {
		this.checkUserLogin();
		assert auditor != null;
		this.actorService.checkMessageFolders(auditor);

		Auditor result;

		result = this.auditorRepository.save(auditor);

		return result;

	}

	public void delete(final Auditor auditor) {
		this.checkUserLogin();

		assert auditor != null;
		assert auditor.getId() != 0;
		Assert.isTrue(this.auditorRepository.exists(auditor.getId()));
		if (auditor.getNotes().isEmpty()) {
			final Collection<AuditRecord> ars = this.auditRecordService.findARecordsByAuditorID(auditor.getId());
			for (final AuditRecord ar : ars)
				this.auditRecordService.delete(ar);

			this.auditorRepository.delete(auditor);
		}

	}

	//Private Methods------------------------------------------------------------------------------
	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}
}
