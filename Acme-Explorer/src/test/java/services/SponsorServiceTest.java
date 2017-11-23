
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
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	@Autowired
	public SponsorService		sponsorService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public UserAccountService	userAccountService;


	@Test
	public void testCreate() {
		super.authenticate("sponsor1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Sponsor sponsor = this.sponsorService.create();
		Assert.isNull(sponsor.getAddress());
		Assert.isNull(sponsor.getEmail());
		Assert.isTrue(!sponsor.getIsBanned());
		Assert.isTrue(sponsor.getMessageFolders().size() == 5);
		Assert.isNull(sponsor.getName());
		Assert.isNull(sponsor.getPhoneNumber());
		Assert.isNull(sponsor.getSurname());
		Assert.isNull(sponsor.getUserAccount());
		Assert.isTrue(sponsor.getSocialIdentities().size() == 0);

	}
	@Test
	public void testFindAll() {
		super.authenticate("sponsor1");
		final Collection<Sponsor> sponsors = this.sponsorService.findAll();

		Assert.notNull(sponsors);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("sponsor1");
		final Sponsor sponsor1 = (Sponsor) this.sponsorService.findAll().toArray()[1];
		final int sponsorId = sponsor1.getId();
		final Sponsor sponsor = this.sponsorService.findOne(sponsorId);
		Assert.notNull(sponsor);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("sponsor1");
		final UserAccount userAccountSponsor = this.userAccountService.create();
		userAccountSponsor.setUsername("paco1");
		userAccountSponsor.setPassword("paco1");
		final Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		final Collection<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);

		userAccountSponsor.setAuthorities(authorities);
		final UserAccount savedUserAccount = this.userAccountService.save(userAccountSponsor);

		final Sponsor sponsor = this.sponsorService.create();
		sponsor.setAddress("false address");
		sponsor.setEmail("email@gmail.com");
		sponsor.setName("Paco");
		sponsor.setPhoneNumber("654789123");
		sponsor.setSurname("Perez");
		sponsor.setUserAccount(savedUserAccount);

		final Sponsor savedSponsor = this.sponsorService.save(sponsor);

		Assert.isTrue(this.sponsorService.findAll().contains(savedSponsor));
		Assert.isTrue(this.actorService.findAll().contains(savedSponsor));
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("sponsor1");
		final Sponsor sponsor = (Sponsor) this.sponsorService.findAll().toArray()[1];

		this.sponsorService.delete(sponsor);
		Assert.isTrue(!this.sponsorService.findAll().contains(sponsor));
		Assert.isTrue(!this.actorService.findAll().contains(sponsor));

		super.unauthenticate();
	}
}
