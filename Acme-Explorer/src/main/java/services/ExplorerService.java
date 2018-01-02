
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ExplorerRepository;
import domain.Application;
import domain.Contact;
import domain.Explorer;
import domain.MessageFolder;
import domain.Search;
import domain.SocialIdentity;
import domain.Story;
import domain.SurvivalClass;

@Service
@Transactional
public class ExplorerService {

	@Autowired
	private ExplorerRepository		explorerRepository;

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SearchService			searchService;


	// Simple CRUD methods --------------------------------------------------

	public Explorer create() {
		Explorer result;
		Search search;

		result = new Explorer();
		search = this.searchService.create();

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
		result.setApplications(new ArrayList<Application>());
		result.setContacts(new ArrayList<Contact>());
		result.setSearch(search);
		result.setSurvivalClasses(new ArrayList<SurvivalClass>());
		result.setStories(new ArrayList<Story>());

		return result;
	}
	public Collection<Explorer> findAll() {

		Collection<Explorer> result;

		result = this.explorerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Explorer findOne(final int explorerId) {

		Assert.isTrue(explorerId != 0);

		Explorer result;

		result = this.explorerRepository.findOne(explorerId);
		Assert.notNull(result);

		return result;
	}

	public Explorer save(final Explorer explorer) {

		Assert.notNull(explorer);
		this.actorService.checkMessageFolders(explorer);

		Explorer result;

		result = this.explorerRepository.save(explorer);

		return result;
	}

	public void delete(final Explorer explorer) {
		Assert.notNull(explorer);
		Assert.isTrue(explorer.getId() != 0);
		Assert.isTrue(this.explorerRepository.exists(explorer.getId()));
		this.actorService.checkUserLogin();

		//Borrar aplicaciones antes?
		this.searchService.delete(explorer.getSearch());
		explorer.setSearch(null);

		this.explorerRepository.delete(explorer);

	}
	// Other business methods --------------------------------------------------

	public Explorer findExplorerByApplication(final Application application) {
		Assert.notNull(application);

		Explorer result;

		result = this.explorerRepository.findExplorerByApplication(application.getId());

		return result;
	}
	public Explorer findExplorerWithSearch(final Search s) {

		return this.explorerRepository.findExplorerBySearch(s.getId());

	}

}
