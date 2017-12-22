
package controllers.manager;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ManagerService;
import services.TagService;
import services.TagValueService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@Controller
@RequestMapping("tagValue/manager")
public class TagValueManagerController extends AbstractController {

	@Autowired
	ManagerService	managerService;
	@Autowired
	ActorService	actorService;
	@Autowired
	TagValueService	tagValueService;
	@Autowired
	TripService		tripService;
	@Autowired
	TagService		tagService;


	// Constructors -----------------------------------------------------------

	public TagValueManagerController() {
		super();
	}

	// Listing -----------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam("tripId") final int tripId) {
		ModelAndView result;
		Trip trip;

		result = new ModelAndView("tagValue/list");

		trip = this.tripService.findOne(tripId);

		result.addObject("tagValues", trip.getTagValues());
		result.addObject("tripId", trip.getId());

		return result;
	}

	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("tagValueId") final int tagValueId, @RequestParam("tripId") final int tripId) {
		ModelAndView result;
		TagValue tagValue;
		Trip trip;
		Manager manager;

		try {
			tagValue = this.tagValueService.findOne(tagValueId);
			trip = this.tripService.findOne(tripId);
			manager = (Manager) this.actorService.findActorByPrincipal();
			Assert.isTrue(trip.getStartDate().after(new Date()));
			Assert.isTrue(trip.getCancelReason() == null || trip.getCancelReason().equals(""));
			Assert.isTrue(manager.getTrips().contains(trip));
			result = this.createEditModelAndView(tagValue, tripId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save", "tripId"
	})
	public ModelAndView save(@RequestParam("tripId") final int tripId, @ModelAttribute("tagValue") @Valid final TagValue tagValue, final BindingResult binding) {

		ModelAndView result;
		Trip trip;
		Collection<Tag> tripTags;

		if (binding.hasErrors())
			result = this.createEditModelAndView(tagValue, tripId);
		else
			try {
				trip = this.tripService.findOne(tripId);

				if (!trip.getTagValues().contains(tagValue)) {
					tripTags = this.tagService.findTagsByTrip(trip.getId());
					Assert.isTrue(!tripTags.contains(tagValue.getTag()));
				}
				this.tagValueService.save(tagValue, trip);
				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(tagValue, tripId, "tagValue.commit.error");

			}

		return result;
	}
	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"delete", "tripId"
	})
	public ModelAndView delete(@RequestParam("tripId") final int tripId, @ModelAttribute("tagValue") @Valid final TagValue tagValue, final BindingResult binding) {
		ModelAndView result;

		try {
			this.tagValueService.delete(tagValue);
			result = new ModelAndView("redirect:/trip/detailed-trip.do?tripId=" + tripId + "&anonymous=false");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(tagValue, tripId, "tagValue.commit.error");
		}

		return result;
	}
	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam("tripId") final int tripId) {
		ModelAndView result;
		TagValue tagValue;
		Trip trip;
		Manager manager;

		try {
			tagValue = this.tagValueService.create();
			trip = this.tripService.findOne(tripId);
			manager = (Manager) this.actorService.findActorByPrincipal();
			Assert.isTrue(trip.getStartDate().after(new Date()));
			Assert.isTrue(manager.getTrips().contains(trip));
			Assert.isTrue(trip.getCancelReason() == null || trip.getCancelReason().equals(""));
			result = this.createEditModelAndView(tagValue, tripId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Ancillary methods ---------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final TagValue tagValue, final int tripId) {
		ModelAndView result;

		result = this.createEditModelAndView(tagValue, tripId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final TagValue tagValue, final int tripId, final String messageCode) {
		ModelAndView result;
		Collection<Tag> tags, tripTags;

		tags = this.tagService.findAll();
		tripTags = this.tagService.findTagsByTrip(tripId);

		tags.removeAll(tripTags);

		result = new ModelAndView("tagValue/edit");

		result.addObject("tagValue", tagValue);
		result.addObject("tags", tags);
		result.addObject("tripId", tripId);
		result.addObject("message", messageCode);

		return result;
	}

}
