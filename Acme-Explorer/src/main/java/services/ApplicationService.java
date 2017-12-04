
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
import domain.Explorer;
import domain.Manager;
import domain.Message;
import domain.MessageFolder;
import domain.Rejection;
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
	private AdministratorService	administratorService;
	@Autowired
	private ExplorerService			explorerService;
	@Autowired
	private RejectionService		rejectionService;


	// Simple CRUD methods --------------------------------------------------

	public Application create() {

		UserAccount userAccount;
		Actor actor;
		Application result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		result = new Application();

		result.setDate(new Date(System.currentTimeMillis() - 1));
		result.setStatus("PENDING");

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
		Application result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(applicationId != 0);
		Assert.isTrue(this.applicationRepository.exists(applicationId));
		//Assert.isTrue(actor instanceof Explorer);

		//		 authority = new Authority();
		//		authority.setAuthority(Authority.EXPLORER);
		//		Assert.isTrue(userAccount.getAuthorities().contains(authority));

		result = this.applicationRepository.findOne(applicationId);

		if (actor instanceof Explorer) {
			explorer = (Explorer) actor;
			Assert.isTrue(explorer.getApplications().contains(result));
		} else {
			manager = (Manager) actor;
			Assert.isTrue(this.managerService.findManagedApplicationsByManager(manager).contains(result));
		}

		return result;

	}
	public Application save(final Application application) {

		Assert.notNull(application);

		if (application.getStatus().equals("REJECTED"))
			Assert.notNull(application.getRejection());
		if (application.getStatus().equals("DUE"))
			Assert.notNull(application.getCreditCard() == null);
		if (application.getStatus().equals("ACCEPTED"))
			Assert.notNull(application.getCreditCard() != null);

		UserAccount userAccount;
		Actor actor;
		Explorer explorer;
		Manager manager;
		Application result;
		Trip trip;
		Application storedApplication;
		Administrator system;
		Message message, messageCopy;
		Collection<Manager> managers;
		MessageFolder messageFolderSystem;
		final Rejection rejection;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(actor instanceof Explorer || actor instanceof Manager);

		application.setDate(new Date(System.currentTimeMillis() - 1));

		storedApplication = this.applicationRepository.findOne(application.getId());

		result = this.applicationRepository.save(application);

		trip = result.getTrip();
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

			explorer.getApplications().add(result);
			this.explorerService.save(explorer);
			if (storedApplication != null && !storedApplication.getStatus().equals(result.getStatus())) {

				system = this.administratorService.findSystemAdministrator();

				managers = this.managerService.findManagersManage(result);
				messageFolderSystem = (MessageFolder) system.getMessageFolders().toArray()[1];

				system = this.administratorService.findSystemAdministrator();
				message = this.messageService.create();
				message.setMessageFolder(messageFolderSystem);
				message.setPriority("HIGH");
				message.setReceiver(explorer);
				message.setSender(system);
				message.setSubject("Status changed");
				message.setBody("The new status is" + result.getStatus() + ".");

				this.actorService.sendMessage(message, system, explorer);

				for (final Manager m : managers) {
					messageCopy = this.messageService.copyMessage(message);
					this.actorService.sendMessage(messageCopy, system, m);
				}
			}

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
	public Application findApplicationByExplorerTrip(final int idExplorer, final int idtrip) {

		Application result;

		result = this.applicationRepository.findApplicationByExplorerTrip(idExplorer, idtrip);

		return result;
	}

}
