
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public SponsorshipService	sponsorshipService;
	@Autowired
	public SponsorService		sponsorService;
	@Autowired
	public TripService			tripService;
	@Autowired
	public CurriculumService	curriculumService;


	@Test
	public void testCreate() {
		super.authenticate("sponsor1");
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.create();

		Assert.notNull(sponsorship);
		Assert.isNull(sponsorship.getBannerUrl());
		Assert.isNull(sponsorship.getAdditionalInfoLink());
		Assert.isNull(sponsorship.getCreditCard());

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAll();

		Assert.notNull(sponsorships);

	}

	@Test
	public void testFindOne() {
		final Sponsorship sponsorship1 = (Sponsorship) this.sponsorshipService.findAll().toArray()[1];
		final int sponsorshipId = sponsorship1.getId();

		final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);
	}

	@Test
	public void testSave() {
		super.authenticate("sponsor1");
		final Sponsorship sponsorship = this.sponsorshipService.create();
		final Sponsor sponsor = (Sponsor) this.sponsorService.findAll().toArray()[1];
		final Trip trip = (Trip) this.tripService.findAll().toArray()[1];
		final CreditCard creditCard = new CreditCard();
		creditCard.setBrandName("visa");
		creditCard.setCvv(382);
		creditCard.setExpirationMonth(06);
		creditCard.setExpirationYear(20);
		creditCard.setHolderName("Leopoldo");
		creditCard.setNumber("4659813284634138");

		sponsorship.setBannerUrl("https://hola.com");
		sponsorship.setAdditionalInfoLink("https://hola2.com");
		sponsorship.setCreditCard(creditCard);
		sponsorship.setSponsor(sponsor);
		sponsorship.setTrip(trip);

		//System.out.println("Sponsorships del Sponsor antes del save: \n" + sponsor.getSponsorships());
		//System.out.println("\nSponsorships del Trip antes del save: \n" + trip.getSponsorships());

		final Sponsorship savedSponsorship = this.sponsorshipService.save(sponsorship);

		//System.out.println("\nSponsorships del Sponsor después del save: \n" + sponsor.getSponsorships());
		//System.out.println("\nSponsorships del Trip después del save: \n" + trip.getSponsorships());

		Assert.isTrue(this.sponsorshipService.findAll().contains(savedSponsorship));
		Assert.isTrue(sponsor.getSponsorships().contains(savedSponsorship));
		Assert.isTrue(trip.getSponsorships().contains(savedSponsorship));

		super.unauthenticate();
	}

	//@Test
	public void testUpdate() {
		super.authenticate("sponsor1");
		Sponsorship sponsorship;
		Sponsorship savedSponsorship;
		sponsorship = (Sponsorship) this.sponsorshipService.findAll().toArray()[1];

		final Sponsor sponsor = sponsorship.getSponsor();
		final Trip trip = sponsorship.getTrip();

		sponsorship.setAdditionalInfoLink("https://hola2.com");

		//System.out.println("Sponsorships del Sponsor antes del save: \n" + sponsor.getSponsorships());
		//System.out.println("\nSponsorships del Trip antes del save: \n" + trip.getSponsorships());

		//System.out.println("\n ======================== \n " + sponsorship + "\n ======================== \n ");

		savedSponsorship = this.sponsorshipService.save(sponsorship);

		//System.out.println("\n ======================== \n " + savedSponsorship + "\n ======================== \n ");

		//System.out.println("\nSponsorships del Sponsor después del save: \n" + sponsor.getSponsorships());
		//System.out.println("\nSponsorships del Trip después del save: \n" + trip.getSponsorships());

		System.out.println(sponsorship);

		System.out.println(savedSponsorship);

		Assert.isTrue(savedSponsorship.getBannerUrl().equals("https://www.hola.com"));
		Assert.isTrue(savedSponsorship.getVersion() != 0);
		Assert.isTrue(this.sponsorshipService.findAll().contains(savedSponsorship));
		Assert.isTrue(sponsor.getSponsorships().contains(savedSponsorship));
		Assert.isTrue(trip.getSponsorships().contains(savedSponsorship));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("sponsor1");
		final Sponsorship sponsorship = (Sponsorship) this.sponsorshipService.findAll().toArray()[1];
		final Sponsor sponsor = sponsorship.getSponsor();
		final Trip trip = sponsorship.getTrip();

		Assert.notNull(sponsorship);
		Assert.notNull(sponsor);
		Assert.notNull(trip);

		this.sponsorshipService.delete(sponsorship);

		Assert.isTrue(!this.sponsorshipService.findAll().contains(sponsorship));
		Assert.isTrue(!sponsor.getSponsorships().contains(sponsorship));
		Assert.isTrue(!trip.getSponsorships().contains(sponsorship));
		super.unauthenticate();
	}

	@Test
	public void testGetSponsorshipFromSponsorId() {

		final Sponsor sponsor = (Sponsor) this.sponsorService.findAll().toArray()[1];
		final int sponsorId = sponsor.getId();
		Collection<Sponsorship> sponsorships;

		Assert.notNull(sponsor);

		sponsorships = this.sponsorshipService.getSponsorshipFromSponsorId(sponsorId);

		Assert.notNull(sponsorships);
		Assert.isTrue(sponsorships.equals(sponsor.getSponsorships()));
	}

}
