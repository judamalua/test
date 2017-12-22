
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TripRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;
import domain.Category;
import domain.CreditCard;
import domain.Explorer;
import domain.Message;
import domain.MessageFolder;
import domain.PersonalRecord;
import domain.Ranger;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	public ActorService				actorService;
	@Autowired
	public ExplorerService			explorerService;
	@Autowired
	public UserAccountService		userAccountService;
	@Autowired
	public RangerService			rangerService;
	@Autowired
	public AdministratorService		adminService;
	@Autowired
	public CurriculumService		curriculumService;
	@Autowired
	public CategoryService			categoryService;
	@Autowired
	public MessageService			messageService;
	@Autowired
	public MessageFolderService		messageFolderService;

	@Autowired
	public PersonalRecordService	personalRecordService;

	@Autowired
	public TripRepository			tripRepository;

	@Autowired
	public SearchService			searchService;


	@Test
	public void saveTest() {
		super.authenticate("ranger1");

		final Explorer explorer = this.explorerService.create();
		explorer.setAddress("dfosd");
		explorer.setEmail("dbfbihsd@gmail.com");
		explorer.setName("Juan");
		explorer.setPhoneNumber("654789123");
		explorer.setSurname("Loper Lopez");
		final UserAccount ua = this.userAccountService.create();

		final Collection<Authority> authorities = ua.getAuthorities();

		final Authority authority = new Authority();
		authority.setAuthority(Authority.EXPLORER);
		authorities.add(authority);

		ua.setAuthorities(authorities);
		explorer.setUserAccount(ua);

		Assert.notNull(explorer);
		this.userAccountService.save(ua);
		final Explorer savedExplorer = (Explorer) this.actorService.save(explorer);
		Assert.isTrue(this.actorService.findAll().contains(savedExplorer));

	}
	@Test
	public void testRegisterExplorer() {

		final Explorer explorer = this.explorerService.create();
		explorer.setAddress("dfosd");
		explorer.setEmail("dbfbihsd@gmail.com");
		explorer.setName("Juan");
		explorer.setPhoneNumber("654789123");
		explorer.setSurname("Loper Lopez");
		final UserAccount ua = this.userAccountService.create();

		final Collection<Authority> authorities = ua.getAuthorities();

		final Authority authority = new Authority();
		authority.setAuthority(Authority.EXPLORER);
		authorities.add(authority);

		ua.setAuthorities(authorities);
		explorer.setUserAccount(ua);

		//		final CreditCard creditCard = new CreditCard();
		//		creditCard.setBrandName(explorer.getName());
		//		creditCard.setCvv(134);
		//		creditCard.setExpirationMonth(11);
		//		creditCard.setExpirationYear(19);
		//		creditCard.setHolderName("Juan");
		//		creditCard.setNumber("4659813284634138");
		//explorer.setCreditCard(creditCard);

		Assert.notNull(explorer);
		this.userAccountService.save(ua);
		//final Explorer savedExplorer = (Explorer) this.actorService.save(explorer);

		final Explorer savedExplorer = this.actorService.registerExplorer(explorer);

		Assert.notNull(this.actorService.findOne(savedExplorer.getId()));

	}

	@Test
	public void testRegisterRanger() {

		final Ranger ranger = this.rangerService.create();
		ranger.setAddress("dfosd");
		ranger.setEmail("dbfbihsd@gmail.com");
		ranger.setIsBanned(false);
		ranger.setName("Juan");
		ranger.setPhoneNumber("654789123");
		ranger.setSurname("Loper Lopez");
		final UserAccount ua = this.userAccountService.create();

		final Collection<Authority> authorities = ua.getAuthorities();

		final Authority authority = new Authority();
		authority.setAuthority(Authority.EXPLORER);
		authorities.add(authority);

		ua.setAuthorities(authorities);
		ranger.setUserAccount(ua);
		final CreditCard creditCard = new CreditCard();
		creditCard.setBrandName(ranger.getName());
		creditCard.setCvv(134);
		creditCard.setExpirationMonth(11);
		creditCard.setExpirationYear(19);
		creditCard.setHolderName("Juan");
		creditCard.setNumber("4659813284634138");
		ranger.setSuspicious(false);
		ranger.setTrips(new ArrayList<Trip>());

		final PersonalRecord personalRecord = this.personalRecordService.create();
		personalRecord.setEmail(ranger.getEmail());
		personalRecord.setLinkedInProfileURL("https://www.linkedInProfile1.com");
		personalRecord.setNameOfCandidate(ranger.getName());
		personalRecord.setPhoneNumber(ranger.getPhoneNumber());
		personalRecord.setPhoto("https://www.photo1.com");

		Assert.notNull(ranger);
		this.userAccountService.save(ua);

		final Ranger savedRanger = this.actorService.registerRanger(ranger);
		Assert.notNull(savedRanger);
		Assert.isTrue(this.actorService.findAll().contains(savedRanger));

	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.notNull(actors);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		Actor actor;
		actor = (Actor) this.actorService.findAll().toArray()[0];
		Assert.notNull(this.actorService.findOne(actor.getId()));
		super.unauthenticate();
	}

	//@Test
	public void testDelete() {
		super.authenticate("admin1");
		final Administrator a = this.adminService.create();
		a.setAddress("addres1");
		a.setEmail("email@email.com");
		a.setName("admin12");
		a.setPhoneNumber("64564846");
		a.setSurname("surname");
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final UserAccount userAccount = this.userAccountService.create();
		userAccount.setUsername("admin12");
		userAccount.setPassword("admin12");
		userAccount.addAuthority(authority);
		a.setUserAccount(userAccount);
		final Administrator saved = (Administrator) this.actorService.save(a);

		Assert.isTrue(this.actorService.findAll().contains(saved));
		Assert.notNull(saved);
		this.actorService.delete(saved);

		Assert.isTrue(!this.actorService.findAll().contains(saved));
		super.unauthenticate();
	}
	@Test
	public void testFindAllTrips() {
		final Collection<Trip> trips = this.actorService.findAllTrips();
		Assert.notNull(trips);
	}
	@Test
	public void testSearchTripsForKeyword() {
		final String keyword = "Title";
		final Collection<Trip> trips = this.actorService.searchTrips(keyword);
		Assert.notNull(trips);
	}
	@Test
	public void testSearchTripsForCategory() {
		final Category category = (Category) this.categoryService.findAll().toArray()[0];
		final Collection<Trip> trips = this.actorService.searchTrips(category);
		Assert.notNull(trips);
	}
	@Test
	public void testSaveMessage() {
		super.authenticate("admin1");
		final Actor actor = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		final MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		final Message message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()));
		message.setMessageFolder(messageFolder);
		message.setSubject("Buenas");

		final Message savedMessage = this.messageService.save(message);

		Assert.isTrue(this.messageService.findAll().contains(savedMessage));
	}

	@Test
	public void testDeleteMessage() {
		super.authenticate("admin1");
		Actor actor, foundActor;
		MessageFolder trashBox;

		actor = (Actor) this.actorService.findAll().toArray()[0];
		foundActor = this.actorService.findOne(actor.getId());
		super.authenticate(actor.getUserAccount().getUsername());
		final MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[1];
		Message message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()));
		message.setMessageFolder(messageFolder);
		message.setSubject("Buenas");
		message = this.messageService.save(message);

		this.messageService.delete(message);

		trashBox = this.messageFolderService.findMessageFolder("trash box", foundActor);

		if (message.getMessageFolder().getName() != "trash box")
			Assert.isTrue(!trashBox.getMessages().contains(message));
		else
			Assert.notNull(this.messageService.findOne(message.getId()));
		super.unauthenticate();
	}

	@Test
	public void testMoveMessage() {
		Actor actor, foundActor;
		actor = (Actor) this.actorService.findAll().toArray()[0];
		foundActor = this.actorService.findOne(actor.getId());
		super.authenticate(foundActor.getUserAccount().getUsername());
		final MessageFolder messageFolder = (MessageFolder) foundActor.getMessageFolders().toArray()[0];
		final MessageFolder messageFolderDest = (MessageFolder) foundActor.getMessageFolders().toArray()[1];

		Message message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()));
		message.setMessageFolder(messageFolder);
		message.setSubject("Buenas");
		message = this.messageService.save(message);

		final Message movedMessage = this.actorService.moveMessage(message, messageFolderDest);

		Assert.isTrue(!messageFolder.getMessages().contains(movedMessage));
	}

	@Test
	public void testSaveMessageFolder() {
		final Actor actor;
		super.authenticate("admin1");
		actor = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
		final MessageFolder messageFolder = this.messageFolderService.create();
		messageFolder.setIsDefault(false);
		messageFolder.setMessageFolderChildren(new ArrayList<MessageFolder>());
		messageFolder.setMessageFolderFather(null);
		messageFolder.setMessages(new ArrayList<Message>());
		messageFolder.setName("NewBox");

		final MessageFolder savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));
		super.unauthenticate();
	}

	@Test
	public void testDeleteMessageFolder() {
		Actor actor;
		super.authenticate("admin1");

		actor = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		final MessageFolder messageFolder = this.messageFolderService.create();
		messageFolder.setIsDefault(false);
		messageFolder.setMessageFolderChildren(new ArrayList<MessageFolder>());
		messageFolder.setMessageFolderFather(null);
		messageFolder.setMessages(new ArrayList<Message>());
		messageFolder.setName("NewBox");

		final MessageFolder savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));

		this.messageFolderService.delete(savedMessageFolder);

		Assert.isTrue(!actor.getMessageFolders().contains(savedMessageFolder));
	}

	@Test
	public void testSendMessage() {
		super.authenticate("ranger1");
		Message message;
		UserAccount userAccount;
		Actor sender, receiver;
		MessageFolder messageFolderSender, messageFolderReceiver;

		userAccount = LoginService.getPrincipal();
		sender = this.actorService.findActorByUserAccountId(userAccount.getId());
		super.authenticate(sender.getUserAccount().getUsername());
		receiver = (Actor) this.actorService.findAll().toArray()[0];
		messageFolderSender = (MessageFolder) sender.getMessageFolders().toArray()[0];

		Assert.isTrue(messageFolderSender.getMessages().size() == 0);

		message = this.messageService.create();
		message.setMessageFolder(messageFolderSender);
		message.setPriority("LOW");
		message.setReceiver(receiver);
		message.setSender(sender);
		message.setSubject("Hola");
		message.setBody("Hola");

		this.actorService.sendMessage(message, sender, receiver);

		messageFolderReceiver = this.messageFolderService.findMessageFolder("in box", receiver);

		Assert.isTrue(messageFolderReceiver.getMessages().size() > 0);
		Assert.isTrue(messageFolderSender.getMessages().size() > 0);
		super.unauthenticate();
	}

	@Test
	public void testFindActorByMessageFolder() {
		super.authenticate("explorer1");

		Actor actor;
		MessageFolder messageFolder;

		messageFolder = (MessageFolder) this.messageFolderService.findAll().toArray()[0];
		actor = this.actorService.findActorByMessageFolder(messageFolder.getId());

		Assert.notNull(actor);
		super.unauthenticate();
	}
}
