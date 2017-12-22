
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Application;
import domain.CreditCard;
import domain.Explorer;
import domain.Manager;
import domain.Message;
import domain.MessageFolder;
import domain.Trip;

@Service
@Transactional
public class ApplicationService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private TripService				tripService;

	// Supporting services --------------------------------------------------
	@Autowired
	private ManagerService			managerService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private ExplorerService			explorerService;


	// Simple CRUD methods --------------------------------------------------

	public Application create() {

		UserAccount userAccount;
		Actor actor;
		Application result;
		CreditCard creditCard;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		creditCard = new CreditCard();
		creditCard.setHolderName("NONE");
		creditCard.setBrandName("NONE");
		creditCard.setNumber("0000000000000000");
		creditCard.setExpirationMonth(1);
		creditCard.setExpirationYear(99);
		creditCard.setCvv(999);

		result = new Application();

		result.setDate(new Date(System.currentTimeMillis() - 1));
		result.setStatus("PENDING");
		result.setCreditCard(creditCard);

		return result;
	}

	public Collection<Application> findAll() {

		UserAccount userAccount;
		Actor actor;
		//final Authority authority;
		Collection<Application> result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.notNull(this.applicationRepository);
		result = this.applicationRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Application findOne(final int applicationId) {

		UserAccount userAccount;
		Actor actor;
		Explorer explorer;
		Manager manager;
		//		final Application application;
		Application result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(applicationId != 0);
		Assert.isTrue(this.applicationRepository.exists(applicationId));

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		if (actor instanceof Explorer) {
			explorer = (Explorer) actor;
			//			application = this.getApplicationTripExplorer(explorer.getId(), result.getTrip().getId());
			Assert.isTrue(!result.getStatus().equals("CANCELLED") || !result.getStatus().equals("REJECTED"));
			Assert.isTrue(explorer.getApplications().contains(result));

		} else {
			manager = (Manager) actor;
			Assert.isTrue(result.getRejection() == null || result.getRejection().getReason() == "");
			Assert.isTrue(this.managerService.findManagedApplicationsByManager(manager).contains(result));
		}

		return result;

	}
	public void changeStatus(final Application application, final String status) {

		Administrator system;
		Message message, messageCopy;
		Collection<Manager> managers;
		MessageFolder messageFolderSystem;
		MessageFolder messageFolderReceiver;
		Explorer explorer;

		Assert.isTrue(!status.equals("PENDING"));
		if (status.equals("DUE"))
			Assert.isTrue(application.getStatus().equals("PENDING"));
		else if (status.equals("ACCEPTED")) {
			Assert.isTrue(application.getStatus().equals("DUE"));
			Assert.isTrue(!application.getCreditCard().getBrandName().equals("NONE"));
			Assert.isTrue(!application.getCreditCard().getHolderName().equals("NONE"));
			Assert.isTrue(!application.getCreditCard().getNumber().equals("0000000000000000"));
		} else if (status.equals("CANCELLED"))
			Assert.isTrue(application.getStatus().equals("ACCEPTED"));
		else if (status.equals("REJECTED"))
			Assert.isTrue(!application.getStatus().equals("REJECTED"));

		if (!application.getStatus().equals(status)) {

			system = this.administratorService.findSystemAdministrator();
			explorer = this.explorerService.findExplorerByApplication(application);

			managers = this.managerService.findManagersManage(application);
			messageFolderSystem = this.messageFolderService.findMessageFolder("notification box", system);
			messageFolderReceiver = this.messageFolderService.findMessageFolder("notification box", explorer);

			system = this.administratorService.findSystemAdministrator();
			message = this.messageService.create();
			message.setMessageFolder(messageFolderSystem);
			message.setPriority("HIGH");
			message.setReceiver(explorer);
			message.setSender(system);
			message.setSubject("Status changed");
			message.setBody("The new status of application with trip: " + application.getTrip().getTitle() + ", is " + status + ".");

			this.actorService.sendMessage(message, system, explorer, messageFolderReceiver);

			for (final Manager m : managers) {
				messageCopy = this.messageService.copyMessage(message);
				messageFolderReceiver = this.messageFolderService.findMessageFolder("notification box", m);
				this.actorService.sendMessage(messageCopy, system, m, messageFolderReceiver);
			}
		}
		application.setStatus(status);
	}
	public Application save(final Application application) {

		Assert.notNull(application);

		Trip tripApplication;
		Date currentDate;

		tripApplication = application.getTrip();
		currentDate = new Date();

		if (!(this.actorService.findActorByPrincipal() instanceof Manager))
			Assert.isTrue(tripApplication.getPublicationDate().before(currentDate) && tripApplication.getStartDate().after(currentDate));
		if (application.getStatus().equals("REJECTED"))
			Assert.notNull(application.getRejection());
		//		if (application.getStatus().equals("DUE"))
		//			Assert.notNull(application.getCreditCard() == null);
		if (application.getStatus().equals("ACCEPTED"))
			Assert.isTrue(application.getCreditCard() != null);

		UserAccount userAccount;
		Actor actor;
		Explorer explorer;
		Manager manager;
		Application result;
		Trip trip;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(actor instanceof Explorer || actor instanceof Manager);

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Explorer) {

			if (application.getCommentaries() != null)
				this.actorService.checkSpamWords(application.getCommentaries());
			this.actorService.checkSpamWords(application.getCreditCard().getBrandName());
			this.actorService.checkSpamWords(application.getCreditCard().getHolderName());

		}

		application.setDate(new Date(System.currentTimeMillis() - 1));

		result = this.applicationRepository.save(application);

		trip = result.getTrip();
		if (trip.getApplications().contains(application))
			trip.getApplications().remove(application);
		trip.getApplications().add(result);
		this.tripService.save(trip);

		if (actor instanceof Manager) {
			manager = (Manager) actor;
			//managers = this.managerService.findManagersManage(application);

			Assert.isTrue(application.getTrip().getManagers().contains(manager));
		} else {
			explorer = (Explorer) actor;
			if (application.getId() != 0)
				Assert.isTrue(this.explorerService.findExplorerByApplication(application).equals(explorer));

			if (explorer.getApplications().contains(application))
				explorer.getApplications().remove(application);
			explorer.getApplications().add(result);
			this.explorerService.save(explorer);

		}

		return result;

	}
	public void delete(final Application application) {

		assert application.getId() != 0;
		Assert.notNull(application);

		UserAccount userAccount;
		Actor actor;
		Explorer explorer;
		//Authority authority;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		//Assert.isTrue(actor instanceof Explorer);

		//		authority = new Authority();
		//		authority.setAuthority(Authority.EXPLORER);
		//		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		explorer = (Explorer) actor;

		Assert.isTrue(this.explorerService.findExplorerByApplication(application).equals(explorer));
		Assert.isTrue(explorer.getApplications().contains(application));

		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		explorer.getApplications().remove(application);
		this.actorService.save(explorer);

		this.applicationRepository.delete(application);

	}

	// Other business methods --------------------------------------------------

	// Requisito funcional 14.6, query C/5.
	public String getRatioPendingApplications() {
		this.actorService.checkUserLogin();

		return this.applicationRepository.getRatioPendingApplications();
	}

	// Requisito funcional 14.6, query C/6.
	public String getRatioDueApplications() {
		this.actorService.checkUserLogin();

		return this.applicationRepository.getRatioDueApplications();
	}

	// Requisito funcional 14.6, query C/7.
	public String getRatioAcceptedApplications() {
		this.actorService.checkUserLogin();

		return this.applicationRepository.getRatioAcceptedApplications();
	}

	// Requisito funcional 14.6, query C/8.
	public String getRatioCancelledApplications() {
		this.actorService.checkUserLogin();

		return this.applicationRepository.getRatioCancelledApplications();
	}
	//Requirement 44
	public Application getApplicationTripExplorer(final int idExplorer, final int idTrip) {
		return this.applicationRepository.findApplicationByExplorerTrip(idExplorer, idTrip);
	}

	public Application managerSave(final Application application, final Explorer explorer) {

		Assert.notNull(application);

		if (application.getStatus().equals("REJECTED"))
			Assert.notNull(application.getRejection());
		if (application.getStatus().equals("DUE"))
			Assert.notNull(application.getCreditCard() == null);
		if (application.getStatus().equals("ACCEPTED"))
			Assert.notNull(application.getCreditCard() != null);

		UserAccount userAccount;
		Actor actor;
		Application result;
		Trip trip;
		final Collection<Application> applications;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		applications = this.managerService.findManagedApplicationsByManager((Manager) actor);
		//		if (application.getId() != 0)
		//			explorer = this.explorerService.findExplorerByApplication(application);

		Assert.isTrue(applications.contains(application));
		application.setDate(new Date(System.currentTimeMillis() - 1));

		result = this.applicationRepository.save(application);

		trip = result.getTrip();
		trip.getApplications().add(result);
		this.tripService.save(trip);

		explorer.getApplications().add(result);
		this.actorService.save(explorer);

		return result;

	}
	public Collection<Application> findApplicationsGroupByStatus(final Explorer e) {

		Assert.notNull(e);
		this.actorService.checkUserLogin();

		Collection<Application> result;

		result = this.applicationRepository.findApplicationsGroupByStatus(e.getId());

		return result;
	}

	public Collection<Application> findApplications(final Explorer e) {

		Assert.notNull(e);
		this.actorService.checkUserLogin();

		Collection<Application> result;

		result = this.applicationRepository.findApplications(e.getId());

		return result;
	}
	public Application findApplicationByExplorerTrip(final int idExplorer, final int idtrip) {

		Application result;

		result = this.applicationRepository.findApplicationByExplorerTrip(idExplorer, idtrip);

		return result;
	}

}
