
package controllers.sponsor;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.SponsorService;
import services.SponsorshipService;
import services.TripService;
import controllers.AbstractController;
import domain.Actor;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Trip;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	SponsorService		sponsorService;
	@Autowired
	ActorService		actorService;
	@Autowired
	SponsorshipService	sponsorshipService;
	@Autowired
	TripService			tripService;


	// Constructors -----------------------------------------------------------

	public SponsorshipSponsorController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("sponsorship/list");

		final Actor actor = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.getSponsorshipFromSponsorId(actor.getId());
		result.addObject("sponsorships", sponsorships);
		result.addObject("requestUri", "sponsorship/sponsor/list.do");

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.create();
			final Sponsor sponsor = (Sponsor) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
			sponsorship.setSponsor(sponsor);
			final Trip trip = this.tripService.findOne(tripId);
			sponsorship.setTrip(trip);
			result = this.createEditModelAndView(sponsorship);

			Assert.isTrue(trip.getPublicationDate().before(new Date()));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			Assert.notNull(sponsorship);
			result = this.createEditModelAndView(sponsorship);
			Assert.isTrue(this.actorService.findActorByPrincipal().getId() == sponsorship.getSponsor().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	//	@RequestMapping(value = "/addCreditCard", method = RequestMethod.GET)
	//	public ModelAndView addCreditCard(@RequestParam final String bannerUrl, @RequestParam final String additionalInfoLink, @RequestParam final int tripId) {
	//		ModelAndView result;
	//
	//		final CreditCard creditcard = new CreditCard();
	//
	//		result = new ModelAndView("sponsorship/addCreditCard");
	//		result.addObject("tripId", tripId);
	//		result.addObject("bannerUrl", bannerUrl);
	//		result.addObject("additionalInfoLink", additionalInfoLink);
	//		result.addObject("creditcard", creditcard);
	//
	//		return result;
	//	}
	// Saving --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Sponsorship sponsorship, final BindingResult binding) {

		ModelAndView result;
		Date currentDate;

		//		if (sponsorship.getCreditCard() == null) {
		//			if (this.chechErrorsBinding(binding))
		//				result = this.createEditModelAndView(sponsorship, "auditRecord.commit.error");
		//			else
		//				result = new ModelAndView("redirect:addCreditCard.do?bannerUrl=" + sponsorship.getBannerUrl() + "&additionalInfoLink=" + sponsorship.getAdditionalInfoLink() + "&tripId=" + sponsorship.getTrip().getId());
		//		} else 
		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				currentDate = new Date();
				Assert.isTrue(sponsorship.getCreditCard().getExpirationYear() >= currentDate.getYear() % 100);
				if (sponsorship.getCreditCard().getExpirationYear() == currentDate.getYear() % 100)
					Assert.isTrue(sponsorship.getCreditCard().getExpirationMonth() > currentDate.getMonth() % 100);
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(sponsorship, "auditRecord.commit.error");

			}

		return result;
	}

	//	@RequestMapping(value = "/addCreditCard", method = RequestMethod.POST, params = "save")
	//	public ModelAndView saveAddCreditCard(@RequestParam @Valid @URL @NotBlank final String bannerUrl, @RequestParam @Valid @URL @NotBlank final String additionalInfoLink, final int tripId, @RequestParam @Valid final CreditCard creditcard,
	//		@RequestParam final BindingResult binding) {
	//
	//		ModelAndView result;
	//
	//		final Sponsorship sponsorship = this.sponsorshipService.create();
	//		sponsorship.setAdditionalInfoLink(additionalInfoLink);
	//		sponsorship.setBannerUrl(bannerUrl);
	//		final Sponsor sponsor = (Sponsor) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
	//		sponsorship.setSponsor(sponsor);
	//		final Trip trip = this.tripService.findOne(tripId);
	//		sponsorship.setTrip(trip);
	//		sponsorship.setCreditCard(creditcard);
	//
	//		if (binding.hasErrors()) {
	//			result = new ModelAndView("sponsorship/addCreditCard");
	//			result.addObject("tripId", tripId);
	//			result.addObject("bannerUrl", bannerUrl);
	//			result.addObject("additionalInfoLink", additionalInfoLink);
	//			result.addObject("creditcard", creditcard);
	//		} else
	//			try {
	//				this.sponsorshipService.save(sponsorship);
	//				result = new ModelAndView("redirect:list.do");
	//			} catch (final Throwable oops) {
	//				result = this.createEditModelAndView(sponsorship, "auditRecord.commit.error");
	//
	//			}
	//
	//		return result;
	//	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		try {
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "auditRecord.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);

		return result;
	}
	//	private boolean chechErrorsBinding(final BindingResult binding) {
	//		boolean res = false;
	//		for (final ObjectError e : binding.getAllErrors())
	//			for (int i = 0; i < e.getCodes().length; i++)
	//				if (e.getCodes()[i].toString().toLowerCase().contains("bannerurl") || e.getCodes()[i].toString().toLowerCase().contains("additionalinfolink"))
	//					res = true;
	//
	//		return res;
	//	}
}
