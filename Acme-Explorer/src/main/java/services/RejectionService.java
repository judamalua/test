
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RejectionRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Application;
import domain.Manager;
import domain.Rejection;
import domain.Trip;

@Service
@Transactional
public class RejectionService {

	// Managed repository --------------------------------------------------

	@Autowired
	private RejectionRepository	rejectionRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService		actorService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ManagerService		managerService;


	//	@Autowired
	//	private ManagerService		managerService;

	// Simple CRUD methods --------------------------------------------------

	public Rejection create() {
		this.actorService.checkUserLogin();

		Rejection result;

		result = new Rejection();

		return result;
	}

	public Collection<Rejection> findAll() {

		Collection<Rejection> result;

		Assert.notNull(this.rejectionRepository);
		result = this.rejectionRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Rejection findOne(final int rejectionId) {

		Rejection result;

		result = this.rejectionRepository.findOne(rejectionId);

		return result;

	}

	public Rejection findOneToEdit(final int rejectionId) {

		Rejection result;
		Actor actor;
		Collection<Trip> trips;

		actor = this.actorService.findActorByPrincipal();
		trips = this.managerService.findTripsByManager(actor.getId());

		result = this.rejectionRepository.findOne(rejectionId);

		Assert.isTrue(actor instanceof Manager);
		Assert.isTrue(trips.contains(result.getApplication().getTrip()));

		return result;

	}

	public Rejection save(final Rejection rejection) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		assert rejection != null;

		Assert.isTrue(rejection.getApplication().getStatus().equals("PENDING") || rejection.getApplication().getStatus().equals("REJECTED"));
		//TODO: RejectionManager is correct
		//Assert.isTrue();

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Manager)
			this.actorService.checkSpamWords(rejection.getReason());

		Rejection result;
		Manager manager;

		final Application application = rejection.getApplication();

		result = this.rejectionRepository.save(rejection);

		if (rejection.getId() != 0) {
			manager = this.managerService.findManagerByRejection(result.getId());
			manager.getRejections().remove(rejection);
			manager.getRejections().add(result);
			this.managerService.save(manager);

		}
		application.setRejection(result);
		manager = (Manager) actor;

		this.applicationService.save(application);
		manager.getRejections().add(result);
		this.managerService.save(manager);

		return result;

	}
	//	public void delete(final Rejection rejection) {
	//		this.actorService.checkUserLogin();
	//		Assert.isTrue(rejection.getApplication().getStatus().equals("REJECTED"));
	//		assert rejection != null;
	//		assert rejection.getId() != 0;
	//		final Application application = this.applicationService.findOne(rejection.getApplication().getId());
	//		//final Manager manager = rejection.getManager();
	//		Assert.isTrue(this.rejectionRepository.exists(rejection.getId()));
	//		application.setRejection(null);
	//		application.setStatus("PENDING");
	//		this.applicationService.save(application);
	//		//	manager.getRejections().remove(rejection);
	//		//		this.managerService.save(manager);
	//		this.rejectionRepository.delete(rejection);
	//
	//	}
}
