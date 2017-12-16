
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
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RangerServiceTest extends AbstractTest {

	@Autowired
	public AdministratorService	administratorService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public UserAccountService	userAccountService;
	@Autowired
	public RangerService		rangerService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Ranger ranger = this.rangerService.create();
		Assert.isNull(ranger.getAddress());
		Assert.isNull(ranger.getEmail());
		Assert.isTrue(!ranger.getIsBanned());
		Assert.isTrue(ranger.getMessageFolders().size() == 5);
		Assert.isNull(ranger.getName());
		Assert.isNull(ranger.getPhoneNumber());
		Assert.isNull(ranger.getSurname());
		Assert.isNull(ranger.getUserAccount());
		Assert.isTrue(ranger.getSocialIdentities().size() == 0);
		Assert.isNull(ranger.getCurriculum());

	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<Ranger> rangers = this.rangerService.findAll();

		Assert.notNull(rangers);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final Ranger ranger = (Ranger) this.rangerService.findAll().toArray()[0];
		Assert.notNull(ranger);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final UserAccount userAccount = this.userAccountService.create();
		userAccount.setUsername("paco1");
		userAccount.setPassword("paco1");

		final Authority authority = new Authority();
		authority.setAuthority(Authority.RANGER);
		final Collection<Authority> authorities = new ArrayList<>();
		authorities.add(authority);

		userAccount.setAuthorities(authorities);
		final UserAccount savedUserAccount = this.userAccountService.save(userAccount);

		final Ranger ranger = this.rangerService.create();
		ranger.setAddress("false address");
		ranger.setEmail("email@gmail.com");
		ranger.setName("Paco");
		ranger.setPhoneNumber("654789123");
		ranger.setSurname("Perez");
		ranger.setUserAccount(savedUserAccount);

		final Ranger savedRanger = this.rangerService.save(ranger);

		Assert.isTrue(this.rangerService.findAll().contains(savedRanger));
		Assert.isTrue(this.actorService.findAll().contains(savedRanger));
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("admin1");
		final Ranger ranger = (Ranger) this.rangerService.findAll().toArray()[4];

		this.rangerService.delete(ranger);

		Assert.isTrue(!this.administratorService.findAll().contains(ranger));
		Assert.isTrue(!this.actorService.findAll().contains(ranger));

		super.unauthenticate();
	}

	// Con este test probamos que, en una relación bidireccional, si cambias los datos en una 
	// tabla, se cambia automáticamente en la tabla con la que tiene relación, por lo que
	// no tenemos que realizar los cambios nosotros en las dos tablas.
	//	@Test
	//	public void testSave() {
	//		super.authenticate("admin1");
	//
	//		final Ranger r = this.rangerService.findOne(6786);
	//		final Trip t = this.tripService.findOne(6792);
	//		System.out.println("Ranger modificado: " + r.getName());
	//		System.out.println("Ranger del Trip t: " + t.getRanger().getName());
	//		r.setName("Pepito");
	//
	//		final Ranger savedRanger = this.rangerService.save(r);
	//
	//		System.out.println("Ranger modificado: " + savedRanger.getName());
	//		System.out.println("Ranger del Trip t: " + t.getRanger().getName());
	//
	//		super.unauthenticate();
	//	}

	@Test
	public void testGetRatioRangersWithCurriculum() {
		super.authenticate("admin1");

		final String s = this.rangerService.getRatioRangersWithCurriculum();

		Assert.notNull(s);

		super.unauthenticate();

	}

	@Test
	public void testgetRatioEndorserRanger() {
		super.authenticate("admin1");

		final String s = this.rangerService.getRatioEndorsersRangers();

		Assert.notNull(s);
		super.unauthenticate();

	}
	@Test
	public void testgetRatioSuspiciousRanger() {
		super.authenticate("admin1");

		final String s = this.rangerService.getRatioSuspiciousRangers();

		Assert.notNull(s);
		super.unauthenticate();

	}
	public void testgetTripsInfoRanger() {
		super.authenticate("admin1");

		final String s = this.rangerService.getTripsInfoFromRanger();

		Assert.notNull(s);
		super.unauthenticate();

	}
	public void testfindSuspiciousRangers() {
		super.authenticate("admin1");

		final Collection<Ranger> s = this.rangerService.findSuspiciousRngers();

		Assert.notNull(s);
		super.unauthenticate();

	}

}
