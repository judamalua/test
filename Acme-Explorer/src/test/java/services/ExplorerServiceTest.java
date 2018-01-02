
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
import domain.Application;
import domain.Explorer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ExplorerServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public UserAccountService	userAccountService;
	@Autowired
	public ExplorerService		explorerService;
	@Autowired
	public ApplicationService	applicationService;


	@Test
	public void testCreate() {
		//		super.authenticate("explorer1");
		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		//		Assert.notNull(actor);

		super.authenticate("explorer1");
		final Explorer explorer = this.explorerService.create();
		Assert.isNull(explorer.getAddress());
		Assert.isNull(explorer.getEmail());
		Assert.isTrue(!explorer.getIsBanned());
		Assert.isTrue(explorer.getMessageFolders().size() == 5);
		Assert.isNull(explorer.getName());
		Assert.isNull(explorer.getPhoneNumber());
		Assert.isNull(explorer.getSurname());
		Assert.isNull(explorer.getUserAccount());
		Assert.isTrue(explorer.getSocialIdentities().size() == 0);
		Assert.isTrue(explorer.getApplications().isEmpty());
		Assert.isTrue(explorer.getContacts().isEmpty());
		Assert.isTrue(explorer.getStories().isEmpty());
		Assert.notNull(explorer.getSearch());
		Assert.isTrue(explorer.getSurvivalClasses().isEmpty());

		super.unauthenticate();
	}
	@Test
	public void testFindAll() {
		final Collection<Explorer> explorers = this.explorerService.findAll();

		Assert.notNull(explorers);
		//		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("ranger1");
		final Explorer explorer1 = (Explorer) this.explorerService.findAll().toArray()[0];
		final int id = explorer1.getId();
		final Explorer explorer = this.explorerService.findOne(id);
		Assert.notNull(explorer);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final UserAccount userAccountExplorer = this.userAccountService.create();
		userAccountExplorer.setUsername("paco1");
		userAccountExplorer.setPassword("paco1");

		final Authority authority = new Authority();
		authority.setAuthority(Authority.EXPLORER);
		final Collection<Authority> authorities = new ArrayList<>();
		authorities.add(authority);

		userAccountExplorer.setAuthorities(authorities);

		final UserAccount savedUserAccount = this.userAccountService.save(userAccountExplorer);
		final Explorer explorer = this.explorerService.create();
		explorer.setAddress("false address");
		explorer.setEmail("email@gmail.com");
		explorer.setName("Paco");
		explorer.setPhoneNumber("654789123");
		explorer.setSurname("Perez");
		explorer.setUserAccount(savedUserAccount);

		final Explorer savedExplorer = this.explorerService.save(explorer);

		Assert.isTrue(this.explorerService.findAll().contains(savedExplorer));
		Assert.isTrue(this.actorService.findAll().contains(savedExplorer));

		super.unauthenticate();
	}
	@Test
	public void testDelete() {

		super.authenticate("admin1");
		final Explorer explorer = (Explorer) this.explorerService.findAll().toArray()[0];

		this.explorerService.delete(explorer);

		Assert.isTrue(!this.explorerService.findAll().contains(explorer));
		Assert.isTrue(!this.actorService.findAll().contains(explorer));
		super.unauthenticate();
	}

	@Test
	public void testFindExplorerByApplication() {
		super.authenticate("admin1");

		Explorer result;
		Application application;

		application = (Application) this.applicationService.findAll().toArray()[0];
		result = this.explorerService.findExplorerByApplication(application);

		Assert.notNull(result);
		super.unauthenticate();
	}
}
