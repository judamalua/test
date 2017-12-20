
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import controllers.AbstractController;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Trip;

@Service
@Transactional
public class SponsorshipService extends AbstractController {

	// Managed repository --------------------------------------------------

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;
	@Autowired
	private SponsorService			sponsorService;
	@Autowired
	private TripService				tripService;


	// Simple CRUD methods --------------------------------------------------

	public Sponsorship create() {
		this.actorService.checkUserLogin();
		Sponsorship result;

		result = new Sponsorship();

		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;

		Assert.notNull(this.sponsorshipRepository);
		result = this.sponsorshipRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {

		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);

		return result;

	}

	public Sponsorship save(final Sponsorship sponsorship) {
		this.actorService.checkUserLogin();
		assert sponsorship != null;

		Sponsorship result;
		final Sponsor sponsor = sponsorship.getSponsor();
		final Trip trip = sponsorship.getTrip();

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Sponsor) {
			this.actorService.checkSpamWords(sponsorship.getBannerUrl());
			this.actorService.checkSpamWords(sponsorship.getAdditionalInfoLink());
			this.actorService.checkSpamWords(sponsorship.getCreditCard().getBrandName());
			this.actorService.checkSpamWords(sponsorship.getCreditCard().getHolderName());
		}

		result = this.sponsorshipRepository.save(sponsorship);

		if (sponsorship.getId() != 0) {
			sponsor.getSponsorships().remove(sponsorship);
			trip.getSponsorships().remove(sponsorship);
		}

		sponsor.getSponsorships().add(result);
		this.sponsorService.save(sponsor);
		trip.getSponsorships().add(result);
		this.tripService.save(trip);

		return result;

	}
	public void delete(final Sponsorship sponsorship) {
		this.actorService.checkUserLogin();
		Sponsor sponsor;
		Trip trip;

		assert sponsorship != null;
		assert sponsorship.getId() != 0;
		sponsor = sponsorship.getSponsor();
		trip = sponsorship.getTrip();

		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));
		sponsor.getSponsorships().remove(sponsorship);
		this.sponsorService.save(sponsor);
		trip.getSponsorships().remove(sponsorship);
		this.tripService.save(trip);

		this.sponsorshipRepository.delete(sponsorship);

	}
	// Other business methods --------------------------------------------------

	public Collection<Sponsorship> getSponsorshipFromSponsorId(final int sponsorId) {
		return this.sponsorshipRepository.getSponsorshipFromSponsorId(sponsorId);
	}

}
