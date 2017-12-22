
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialIdentityRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.SocialIdentity;

@Service
@Transactional
public class SocialIdentityService {

	@Autowired
	private SocialIdentityRepository	socialIdentityRepository;
	@Autowired
	private ActorService				actorService;


	// Simple CRUD methods --------------------------------------------------

	public SocialIdentity create() {
		SocialIdentity result;

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		result = new SocialIdentity();

		return result;
	}
	public Collection<SocialIdentity> findAll() {

		Collection<SocialIdentity> result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		//		final Authority authority = new Authority();
		//		authority.setAuthority(Authority.ADMIN);
		//
		//		Assert.isTrue(userAccount.getAuthorities().contains(authority)); TODO Revisar

		result = this.socialIdentityRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SocialIdentity findOne(final int socialIdentityId) {

		Assert.isTrue(socialIdentityId != 0);
		Assert.isTrue(this.socialIdentityRepository.exists(socialIdentityId));
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		SocialIdentity result;

		result = this.socialIdentityRepository.findOne(socialIdentityId);
		Assert.notNull(result);
		Assert.isTrue(actor.getSocialIdentities().contains(result));

		return result;
	}

	public SocialIdentity save(final SocialIdentity socialIdentity) {

		Assert.notNull(socialIdentity);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(socialIdentity.getNick());
		this.actorService.checkSpamWords(socialIdentity.getName());
		this.actorService.checkSpamWords(socialIdentity.getProfileLink());
		if (socialIdentity.getPhotoUrl() != null)
			this.actorService.checkSpamWords(socialIdentity.getPhotoUrl());

		SocialIdentity result;

		result = this.socialIdentityRepository.save(socialIdentity);
		actor.getSocialIdentities().remove(socialIdentity);
		actor.getSocialIdentities().add(result);
		this.actorService.save(actor);

		return result;
	}

	public void delete(final SocialIdentity socialIdentity) {
		Assert.notNull(socialIdentity);
		Assert.isTrue(socialIdentity.getId() != 0);
		Assert.isTrue(this.socialIdentityRepository.exists(socialIdentity.getId()));

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(actor.getSocialIdentities().contains(socialIdentity));

		actor.getSocialIdentities().remove(socialIdentity);
		this.actorService.save(actor);

		this.socialIdentityRepository.delete(socialIdentity);
	}

}
