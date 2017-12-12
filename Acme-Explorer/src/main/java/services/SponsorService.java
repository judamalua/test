
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import domain.MessageFolder;
import domain.SocialIdentity;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SponsorRepository		sponsorRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------

	public Sponsor create() {
		Sponsor result;
		final Collection<Sponsorship> sponsorships = new HashSet<Sponsorship>();
		final Collection<SocialIdentity> socialIdentities = new HashSet<SocialIdentity>();

		result = new Sponsor();
		result.setIsBanned(false);
		result.setSuspicious(false);
		final MessageFolder inBox = this.messageFolderService.create();
		inBox.setIsDefault(true);
		inBox.setMessageFolderFather(null);
		inBox.setName("in box");
		final MessageFolder outBox = this.messageFolderService.create();
		outBox.setIsDefault(true);
		outBox.setMessageFolderFather(null);
		outBox.setName("out box");
		final MessageFolder notificationBox = this.messageFolderService.create();
		notificationBox.setIsDefault(true);
		notificationBox.setMessageFolderFather(null);
		notificationBox.setName("notification box");
		final MessageFolder trashBox = this.messageFolderService.create();
		trashBox.setIsDefault(true);
		trashBox.setMessageFolderFather(null);
		trashBox.setName("trash box");
		final MessageFolder spamBox = this.messageFolderService.create();
		spamBox.setIsDefault(true);
		spamBox.setMessageFolderFather(null);
		spamBox.setName("spam box");

		final Collection<MessageFolder> messageFolders = new ArrayList<MessageFolder>();
		messageFolders.add(inBox);
		messageFolders.add(outBox);
		messageFolders.add(trashBox);
		messageFolders.add(spamBox);
		messageFolders.add(notificationBox);

		final Collection<MessageFolder> savedMessageFolders = new ArrayList<MessageFolder>();

		for (final MessageFolder mf : messageFolders)
			savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

		result.setMessageFolders(savedMessageFolders);
		result.setSponsorships(sponsorships);
		result.setSocialIdentities(socialIdentities);

		return result;
	}

	public Collection<Sponsor> findAll() {

		Collection<Sponsor> result;

		result = this.sponsorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Sponsor findOne(final int sponsorId) {

		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);

		return result;

	}

	public Sponsor save(final Sponsor sponsor) {

		assert sponsor != null;
		this.actorService.checkMessageFolders(sponsor);

		Sponsor result;

		result = this.sponsorRepository.save(sponsor);

		return result;

	}

	public void delete(final Sponsor sponsor) {

		assert sponsor != null;
		assert sponsor.getId() != 0;

		Assert.isTrue(this.sponsorRepository.exists(sponsor.getId()));

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.getSponsorshipFromSponsorId(sponsor.getId());

		for (final Sponsorship s : sponsorships)
			this.sponsorshipService.delete(s);
		//Assert.isTrue(!sponsor.getSponsorships().contains(s));

		this.sponsorRepository.delete(sponsor);

	}
}
