
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Manager;
import domain.MessageFolder;
import domain.Note;
import domain.Rejection;
import domain.SocialIdentity;
import domain.SurvivalClass;
import domain.Trip;

@Service
@Transactional
public class ManagerService {

	// Managed repository --------------------------------------------------
	@Autowired
	private ManagerRepository		managerRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private NoteService				noteService;
	@Autowired
	private RejectionService		rejectionService;
	@Autowired
	private TripService				tripService;


	// Simple CRUD methods --------------------------------------------------

	public Manager create() {
		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));
		this.actorService.checkUserLogin();

		final Collection<Trip> trips = new HashSet<Trip>();
		final Collection<Note> notes = new HashSet<Note>();
		final Collection<SurvivalClass> survivalClasses = new HashSet<SurvivalClass>();
		final Collection<Rejection> rejections = new HashSet<Rejection>();
		Manager result;

		result = new Manager();
		result.setTrips(trips);
		result.setRepliedNotes(notes);
		result.setSurvivalClasses(survivalClasses);
		result.setRejections(rejections);
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
	public Collection<Manager> findAll() {

		Collection<Manager> result;

		result = this.managerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Manager findOne(final int managerId) {

		Assert.isTrue(managerId != 0);

		Manager result;

		result = this.managerRepository.findOne(managerId);
		Assert.notNull(result);

		return result;
	}

	public Manager save(final Manager manager) {
		this.actorService.checkUserLogin();
		Assert.notNull(manager);
		this.actorService.checkMessageFolders(manager);

		Manager result;
		Collection<Rejection> rejections, savedRejections;
		Rejection savedRejection;
		Collection<Trip> trips;

		rejections = manager.getRejections();
		savedRejections = new HashSet<Rejection>();

		for (final Rejection rejection : rejections)
			if (rejection.getId() == 0 || !rejection.equals(this.rejectionService.findOne(rejection.getId()))) {
				savedRejection = this.rejectionService.save(rejection);
				savedRejections.add(savedRejection);
			}

		trips = this.findTripsByManager(manager.getId());

		manager.setRejections(savedRejections);

		result = this.managerRepository.save(manager);
		if (result.getTrips().equals(trips))
			for (final Trip t : trips) {
				if (!manager.getTrips().contains(t))
					if (t.getManagers().contains(manager))
						t.getManagers().remove(manager);
				t.getManagers().add(result);
				this.tripService.save(t);
			}
		return result;
	}
	public void delete(final Manager manager) {
		this.actorService.checkUserLogin();
		Assert.notNull(manager);
		Assert.isTrue(manager.getId() != 0);
		Assert.isTrue(this.managerRepository.exists(manager.getId()));

		Collection<Note> notes;
		Collection<Trip> trips;

		notes = manager.getRepliedNotes();

		for (final Note note : notes) {
			note.setReplierManager(null);
			this.noteService.save(note);
		}

		trips = manager.getTrips();
		for (final Trip t : trips)
			t.getManagers().remove(manager);

		this.managerRepository.delete(manager);
	}
	public Collection<Application> findManagedApplicationsByManager(final Manager m) {

		Assert.notNull(m);
		Assert.notEmpty(m.getTrips());

		Collection<Application> result;

		result = this.managerRepository.findApplicationsManagedByManager(m.getId());

		return result;
	}
	// Other business methods --------------------------------------------------

	public String getRatioSuspiciousManagers() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		return this.managerRepository.getRatioSuspiciousManagers();
	}

	// Requisito funcional 14.6, segunda query.
	public String getTripsInfoFromManager() {
		this.actorService.checkUserLogin();

		return this.managerRepository.getTripsInfoFromManager();
	}

	public Collection<Manager> findManagersManage(final Application application) {

		Assert.notNull(application);

		Collection<Manager> result;

		result = this.managerRepository.findManagersManage(application.getId());

		return result;
	}

	public Collection<Manager> findSuspiciousManagers() {
		this.actorService.checkUserLogin();
		return this.managerRepository.findSuspiciousManagers();
	}

	public Manager findManagerByRejection(final int rejectionId) {
		Assert.isTrue(rejectionId != 0);

		Manager result;

		result = this.managerRepository.findManagerByRejection(rejectionId);

		return result;
	}

	public Manager findManagerBySurvivalClass(final int survivalClassId) {
		Assert.isTrue(survivalClassId != 0);

		Manager result;

		result = this.managerRepository.findManagerBySurvivalClass(survivalClassId);

		return result;
	}

	public Collection<Manager> findManagerByTrip(final int tripId) {

		Collection<Manager> result;

		result = this.managerRepository.findManagerByTrip(tripId);

		return result;
	}

	public Collection<Trip> findTripsByManager(final int managerId) {

		Collection<Trip> result;

		result = this.managerRepository.findTripsByManager(managerId);

		return result;
	}

	public Page<Trip> findTripsByManager(final int managerId, final Pageable pageable) {

		Page<Trip> result;

		result = this.managerRepository.findTripsByManager(managerId, pageable);

		return result;
	}
}
