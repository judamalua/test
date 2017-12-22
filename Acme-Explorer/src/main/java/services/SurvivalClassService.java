
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SurvivalClassRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Explorer;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@Service
@Transactional
public class SurvivalClassService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SurvivalClassRepository	survivalClassRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ManagerService			managerService;
	@Autowired
	private TripService				tripService;
	@Autowired
	private ExplorerService			explorerService;
	@Autowired
	private ApplicationService		applicationService;


	// Simple CRUD methods --------------------------------------------------

	public SurvivalClass create() {
		this.checkUserLogin();

		SurvivalClass result;
		final Collection<Explorer> explorers = new HashSet<Explorer>();

		result = new SurvivalClass();
		result.setExplorers(explorers);

		return result;
	}

	public Collection<SurvivalClass> findAll() {
		this.checkUserLogin();

		Collection<SurvivalClass> result;

		Assert.notNull(this.survivalClassRepository);
		result = this.survivalClassRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public SurvivalClass findOne(final int survivalClassId) {
		this.checkUserLogin();

		SurvivalClass result;

		result = this.survivalClassRepository.findOne(survivalClassId);

		return result;

	}

	public SurvivalClass save(final SurvivalClass survivalClass) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Authority authority = new Authority();
		authority.setAuthority(Authority.EXPLORER);

		assert survivalClass != null;

		if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
			Assert.notNull(this.tripService.findTripApplicationSurvival(survivalClass));
			Assert.isTrue(this.applicationService.getApplicationTripExplorer(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId(), this.tripService.findTripApplicationSurvival(survivalClass).getId()).getStatus()
				.equals("ACCEPTED"));
		}

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Manager) {
			this.actorService.checkSpamWords(survivalClass.getLocation().getName());
			this.actorService.checkSpamWords(survivalClass.getTitle());
			this.actorService.checkSpamWords(survivalClass.getDescription());
		}

		SurvivalClass result;
		final Collection<Trip> trips;
		final Manager manager;
		final Collection<Explorer> explorers;
		Explorer explorer;

		trips = this.tripService.findTripsBySurvivalClass(survivalClass);
		//		if (survivalClass.getId() != 0)
		//			manager = this.managerService.findManagerBySurvivalClass(survivalClass.getId());
		//		else
		manager = (Manager) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		if (!LoginService.getPrincipal().getAuthorities().contains(authority))
			if (survivalClass.getId() != 0)
				Assert.isTrue(manager.getSurvivalClasses().contains(survivalClass));

		explorers = survivalClass.getExplorers();

		if (LoginService.getPrincipal().getAuthorities().contains(authority)) {

			explorer = (Explorer) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

			// Requisito 44.1
			explorer.getSurvivalClasses().add(survivalClass);
			//if (!survivalClass.getExplorers().contains(explorer) && explorer != null)
			survivalClass.getExplorers().add(explorer);

			this.explorerService.save(explorer);

		}

		result = this.survivalClassRepository.save(survivalClass);

		//Requirement 43
		if (survivalClass.getId() != 0) {

			if (manager.getSurvivalClasses().contains(survivalClass)) {
				manager.getSurvivalClasses().remove(survivalClass);
				manager.getSurvivalClasses().add(result);
				this.managerService.save(manager);
			}

			for (final Explorer e : explorers) {

				if (this.findSurvivalClassesByExplorerID(e.getId()).contains(survivalClass))
					e.getSurvivalClasses().remove(survivalClass);

				e.getSurvivalClasses().add(result);
				this.explorerService.save(e);
			}
			// REVISAR MAS TARDE
			//			for (final Trip t : trips) {
			//
			//				if (t.getSurvivalClasses().contains(survivalClass))
			//					t.getSurvivalClasses().remove(survivalClass);
			//
			//				t.getSurvivalClasses().add(result);
			//				this.tripService.save(t);
			//			}

		} else {
			manager.getSurvivalClasses().add(result);
			this.managerService.save(manager);
		}
		return result;

	}
	public void delete(final SurvivalClass survivalClass) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		assert survivalClass != null;
		assert survivalClass.getId() != 0;

		Assert.isTrue(this.survivalClassRepository.exists(survivalClass.getId()));

		Manager manager;
		Collection<Explorer> explorers;
		Collection<Trip> trips;

		manager = this.managerService.findManagerBySurvivalClass(survivalClass.getId());
		Assert.isTrue(manager.equals(actor));
		explorers = survivalClass.getExplorers();
		trips = this.tripService.findTripsBySurvivalClass(survivalClass);

		manager.getSurvivalClasses().remove(survivalClass);

		this.managerService.save(manager);

		if (trips.size() != 0)
			for (final Trip t : trips) {
				t.getSurvivalClasses().remove(survivalClass);
				this.tripService.save(t);
			}

		if (survivalClass.getExplorers().size() != 0)
			for (final Explorer e : explorers) {
				e.getSurvivalClasses().remove(survivalClass);
				this.explorerService.save(e);
			}

		this.survivalClassRepository.delete(survivalClass);

	}

	// Other Business Methods --------------------------------------------------
	public Collection<SurvivalClass> findSurvivalClassByManagerID() {
		this.checkUserLogin();
		final int id = LoginService.getPrincipal().getId();
		final int idManager = this.actorService.findActorByUserAccountId(id).getId();
		return this.survivalClassRepository.findSurvivalClassByManagerID(idManager);

	}

	public Collection<SurvivalClass> findSurvivalClassesByExplorerID(final int id) {
		this.checkUserLogin();
		return this.survivalClassRepository.findSurvivalClassesByExplorerID(id);
	}

	//Requirement 44
	//	public SurvivalClass joinToASurvivalClass(final SurvivalClass survivalClass) {
	//		this.checkUserLogin();
	//
	//		assert survivalClass != null;
	//		Assert.notNull(this.tripService.findTripApplicationSurvival(survivalClass));
	//		Assert.isTrue(this.applicationService.getApplicationTripExplorer(LoginService.getPrincipal().getId(), this.tripService.findTripApplicationSurvival(survivalClass).getId()).getStatus().equals("ACCEPTED"));
	//
	//		SurvivalClass result;
	//		Explorer explorer;
	//
	//		explorer = this.explorerService.findOne(LoginService.getPrincipal().getId());
	//
	//		explorer.getSurvivalClasses().add(survivalClass);
	//		survivalClass.getExplorers().add(explorer);
	//
	//		this.explorerService.save(explorer);
	//
	//		result = this.survivalClassRepository.save(survivalClass);
	//
	//		return result;
	//
	//	}

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

	public Collection<SurvivalClass> findSurvivalClassesByTrip(final Trip trip) {
		Assert.notNull(trip);

		Collection<SurvivalClass> result;

		result = this.survivalClassRepository.findSurvivalClassesByTripId(trip.getId());

		return result;
	}

}
