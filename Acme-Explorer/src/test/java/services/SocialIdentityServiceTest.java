
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
import domain.SocialIdentity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SocialIdentityServiceTest extends AbstractTest {

	@Autowired
	public ActorService				actorService;
	@Autowired
	public SocialIdentityService	socialIdentityService;
	@Autowired
	public ExplorerService			explorerService;
	@Autowired
	public TripService				tripService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		SocialIdentity socialIdentity;
		socialIdentity = this.socialIdentityService.create();

		Assert.notNull(socialIdentity);
		Assert.isNull(socialIdentity.getName());
		Assert.isNull(socialIdentity.getNick());
		Assert.isNull(socialIdentity.getPhotoUrl());
		Assert.isNull(socialIdentity.getProfileLink());

		super.unauthenticate();

	}

	@Test
	public void testFindAll() {

		super.authenticate("admin1");

		final Collection<SocialIdentity> stories = this.socialIdentityService.findAll();

		Assert.notNull(stories);

		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("explorer1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		final SocialIdentity socialIdentiry = (SocialIdentity) actor.getSocialIdentities().toArray()[0];
		final int socialIdentityId = socialIdentiry.getId();

		final SocialIdentity socialIdentity = this.socialIdentityService.findOne(socialIdentityId);
		Assert.notNull(socialIdentity);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final SocialIdentity socialIdentity = this.socialIdentityService.create();

		socialIdentity.setName("Facebook");
		socialIdentity.setNick("JuanF");
		socialIdentity.setProfileLink("https://www.facebook.com");

		final SocialIdentity savedSocialIdentity = this.socialIdentityService.save(socialIdentity);

		Assert.isTrue(this.socialIdentityService.findAll().contains(savedSocialIdentity));
		Assert.isTrue(actor.getSocialIdentities().contains(savedSocialIdentity));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("explorer1");

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final SocialIdentity socialIdentity = (SocialIdentity) actor.getSocialIdentities().toArray()[0];

		this.socialIdentityService.delete(socialIdentity);

		Assert.isTrue(!this.socialIdentityService.findAll().contains(socialIdentity));
		Assert.isTrue(!actor.getSocialIdentities().contains(socialIdentity));

		super.unauthenticate();
	}

}
