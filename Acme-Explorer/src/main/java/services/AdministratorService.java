
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.MessageFolder;
import domain.SocialIdentity;

@Service
@Transactional
public class AdministratorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private ActorService			actorService;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Administrator create() {

		Administrator result;
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		result = new Administrator();
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

		final Collection<MessageFolder> messageFolders = new ArrayList<MessageFolder>();
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
	public Collection<Administrator> findAll() {

		Collection<Administrator> result;
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.notNull(this.administratorRepository);
		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Administrator findOne(final int administratorId) {

		Assert.isTrue(administratorId != 0);
		Assert.isTrue(this.administratorRepository.exists(administratorId));

		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Administrator result;

		result = this.administratorRepository.findOne(administratorId);

		return result;

	}

	public Administrator save(final Administrator administrator) {

		assert administrator != null;
		this.actorService.checkMessageFolders(administrator);

		UserAccount userAccount;
		final Actor actor;
		Administrator result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		result = this.administratorRepository.save(administrator);

		return result;

	}

	public void delete(final Administrator administrator) {

		assert administrator != null;
		assert administrator.getId() != 0;

		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(this.administratorRepository.exists(administrator.getId()));

		this.administratorRepository.delete(administrator);

	}

	//Other Business Methods-----------------------------------------------------------------
	public Administrator findSystemAdministrator() {
		this.actorService.checkUserLogin();

		Administrator result;

		result = this.administratorRepository.findSystemAdministrator();

		Assert.notNull(result);

		return result;
	}

}
