
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Explorer;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SurvivalClassServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public SurvivalClassService	survivalClassService;
	@Autowired
	public ManagerService		managerService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public ExplorerService		explorerService;


	@Test
	public void testCreate() {
		super.authenticate("manager1");
		SurvivalClass survivalClass;
		survivalClass = this.survivalClassService.create();

		Assert.notNull(survivalClass);
		Assert.isNull(survivalClass.getTitle());
		Assert.isNull(survivalClass.getDescription());
		Assert.isNull(survivalClass.getOrganisationMoment());
		Assert.isNull(survivalClass.getLocation());
		Assert.isTrue(survivalClass.getExplorers().size() == 0);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("manager1");

		final Collection<SurvivalClass> survivalClass = this.survivalClassService.findAll();

		Assert.notNull(survivalClass);

		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("manager1");

		final SurvivalClass survivalClass1 = (SurvivalClass) this.survivalClassService.findAll().toArray()[1];
		final int survivalClassId = survivalClass1.getId();

		final SurvivalClass survivalClass = this.survivalClassService.findOne(survivalClassId);
		Assert.notNull(survivalClass);

		super.unauthenticate();
	}

	@Test
	public void testSaveSurvivalClassByManager() {
		super.authenticate("manager1");
		final SurvivalClass svc = (SurvivalClass) this.survivalClassService.findAll().toArray()[0];
		final SurvivalClass saved = this.survivalClassService.save(svc);
		final int id = saved.getId();
		Assert.notNull(this.survivalClassService.findOne(id));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("manager1");

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Manager manager = (Manager) actor;
		final SurvivalClass survivalClass = (SurvivalClass) this.survivalClassService.findAll().toArray()[0];

		final Collection<Trip> trips = this.tripService.findTripsBySurvivalClass(survivalClass);
		final Collection<Explorer> explorers = survivalClass.getExplorers();

		Assert.notNull(survivalClass);
		Assert.notNull(manager);
		Assert.notNull(trips);
		Assert.notNull(explorers);

		this.survivalClassService.delete(survivalClass);

		Assert.isTrue(!this.survivalClassService.findAll().contains(survivalClass));
		Assert.isTrue(!manager.getSurvivalClasses().contains(survivalClass));
		for (final Trip t : trips)
			Assert.isTrue(!t.getSurvivalClasses().contains(survivalClass));
		if (explorers.size() != 0)
			for (final Explorer e : explorers)
				Assert.isTrue(!e.getSurvivalClasses().contains(survivalClass));
		super.unauthenticate();
	}

	@Test
	public void testFindSurvivalClassByManagerID() {
		super.authenticate("manager1");
		Collection<SurvivalClass> result;

		result = this.survivalClassService.findSurvivalClassByManagerID();

		Assert.notNull(result);
		Assert.notEmpty(result);

		super.unauthenticate();
	}

	@Test
	public void testFindSurvivalClassesByExplorerID() {
		super.authenticate("explorer1");
		Collection<SurvivalClass> result;

		result = this.survivalClassService.findSurvivalClassesByExplorerID(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId());

		Assert.notNull(result);
		Assert.notEmpty(result);

		super.unauthenticate();
	}

	@Test
	public void testFindSurvivalClassesByTrip() {
		final Trip trip = (Trip) this.tripService.findAll().toArray()[0];
		Collection<SurvivalClass> result;

		result = this.survivalClassService.findSurvivalClassesByTrip(trip);

		Assert.notNull(result);
		Assert.notEmpty(result);
	}

}
