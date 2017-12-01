
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
import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	public AdministratorService	administratorService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public UserAccountService	userAccountService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Administrator administrator = this.administratorService.create();
		Assert.isNull(administrator.getAddress());
		Assert.isNull(administrator.getEmail());
		Assert.isTrue(!administrator.getIsBanned());
		Assert.isTrue(administrator.getMessageFolders().size() == 5);
		Assert.isNull(administrator.getName());
		Assert.isNull(administrator.getPhoneNumber());
		Assert.isNull(administrator.getSurname());
		Assert.isNull(administrator.getUserAccount());
		Assert.isTrue(administrator.getSocialIdentities().size() == 0);

	}
	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<Administrator> administrators = this.administratorService.findAll();

		Assert.notNull(administrators);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final Administrator administrator = (Administrator) this.administratorService.findAll().toArray()[0];
		Assert.notNull(administrator);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final UserAccount userAccountAdmin = this.userAccountService.create();
		userAccountAdmin.setUsername("paco1");
		userAccountAdmin.setPassword("paco1");

		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Collection<Authority> authorities = new ArrayList<>();
		authorities.add(authority);

		userAccountAdmin.setAuthorities(authorities);
		final UserAccount savedUserAccount = this.userAccountService.save(userAccountAdmin);

		final Administrator administrator = this.administratorService.create();
		administrator.setAddress("false address");
		administrator.setEmail("email@gmail.com");
		administrator.setName("Paco");
		administrator.setPhoneNumber("654789123");
		administrator.setSurname("Perez");
		administrator.setUserAccount(savedUserAccount);

		final Administrator savedAdministrator = this.administratorService.save(administrator);

		Assert.isTrue(this.administratorService.findAll().contains(savedAdministrator));
		Assert.isTrue(this.actorService.findAll().contains(savedAdministrator));
		super.unauthenticate();
	}
	@Test
	public void testDelete() {
		super.authenticate("admin1");
		final Administrator administrator = (Administrator) this.administratorService.findAll().toArray()[1];

		this.administratorService.delete(administrator);

		Assert.isTrue(!this.administratorService.findAll().contains(administrator));
		Assert.isTrue(!this.actorService.findAll().contains(administrator));
	}
}
