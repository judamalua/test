
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public UserAccountService	userAccountService;
	@Autowired
	public ManagerService		managerService;


	public void testCreate() {
		//		super.authenticate("manager1");
		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		//		Assert.notNull(actor);

		final Manager manager = this.managerService.create();
		Assert.isNull(manager.getAddress());
		Assert.isNull(manager.getEmail());
		Assert.isTrue(!manager.getIsBanned());
		Assert.isTrue(manager.getMessageFolders().size() == 0);
		Assert.isNull(manager.getName());
		Assert.isNull(manager.getPhoneNumber());
		Assert.isNull(manager.getSurname());
		Assert.isNull(manager.getUserAccount());
		Assert.isTrue(manager.getSocialIdentities().size() == 0);
		Assert.isTrue(manager.getRejections().isEmpty());
		Assert.isTrue(manager.getTrips().isEmpty());
		Assert.isTrue(manager.getSurvivalClasses().isEmpty());
		Assert.isTrue(manager.getRepliedNotes().isEmpty());

	}
	@Test
	public void testFindAll() {
		final Collection<Manager> managers = this.managerService.findAll();

		Assert.notNull(managers);
		//		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final Manager manager1 = (Manager) this.managerService.findAll().toArray()[0];
		final int id = manager1.getId();
		final Manager manager = this.managerService.findOne(id);
		Assert.notNull(manager);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final UserAccount userAccountManager = this.userAccountService.create();
		userAccountManager.setUsername("paco1");
		userAccountManager.setPassword("paco1");

		final Authority authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		final Collection<Authority> authorities = new ArrayList<>();
		authorities.add(authority);

		userAccountManager.setAuthorities(authorities);
		final UserAccount savedUserAccount = this.userAccountService.save(userAccountManager);

		final Manager manager = this.managerService.create();
		manager.setAddress("false address");
		manager.setEmail("email@gmail.com");
		manager.setName("Paco");
		manager.setPhoneNumber("654789123");
		manager.setSurname("Perez");
		manager.setUserAccount(savedUserAccount);

		final Manager savedManager = this.managerService.save(manager);

		Assert.isTrue(this.managerService.findAll().contains(savedManager));
		Assert.isTrue(this.actorService.findAll().contains(savedManager));

		super.unauthenticate();
	}
	@Test
	public void testDelete() {
		super.authenticate("admin1");
		final Manager manager = (Manager) this.managerService.findAll().toArray()[1];

		this.managerService.delete(manager);

		Assert.isTrue(!this.managerService.findAll().contains(manager));
		Assert.isTrue(!this.actorService.findAll().contains(manager));
		super.unauthenticate();
	}

	@Test
	public void testFindSuspiciousManager() {
		super.authenticate("admin1");
		final Manager m = (Manager) this.managerService.findAll().toArray()[0];
		m.setSuspicious(true);
		final Manager savedManager = this.managerService.save(m);
		Assert.isTrue(this.managerService.findSuspiciousManagers().contains(savedManager));
		super.unauthenticate();
	}

}
