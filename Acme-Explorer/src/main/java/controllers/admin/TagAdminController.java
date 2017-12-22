
package controllers.admin;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.TagService;
import services.TripService;
import controllers.AbstractController;
import domain.Tag;
import domain.Trip;

@Controller
@RequestMapping("/tag/admin")
public class TagAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	TagService				tagService;
	@Autowired
	TripService				tripService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;


	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Tag> tags;
		final Collection<Tag> tagsOnTrips;
		Collection<Trip> trips;

		trips = this.tripService.findAll();
		tagsOnTrips = new HashSet<Tag>();

		for (final Trip t : trips)
			if (!t.getTagValues().isEmpty())
				for (final Tag tag : this.tagService.findTagsByTrip(t.getId()))
					tagsOnTrips.add(tag);

		result = new ModelAndView("tag/list");

		tags = this.tagService.findAll();

		result.addObject("tags", tags);
		result.addObject("tagsOnTrips", tagsOnTrips);

		return result;
	}
	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tagId) {
		ModelAndView result;
		Tag tag;
		Collection<Trip> trips;

		try {
			tag = this.tagService.findOne(tagId);
			trips = this.tripService.findTripsByTagId(tagId);
			Assert.notNull(tag);
			Assert.isTrue(trips.isEmpty());
			result = this.createEditModelAndView(tag);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Tag tag;

		tag = this.tagService.create();
		result = this.createEditModelAndView(tag);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("tagId") final int tagId) {
		ModelAndView result;
		Tag tag;

		tag = this.tagService.findOne(tagId);
		this.tagService.delete(tag);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Tag tag, final BindingResult binding) {
		ModelAndView result;
		final Collection<Trip> trips;

		if (binding.hasErrors())
			result = this.createEditModelAndView(tag, "tag.params.error");
		else
			try {
				this.tagService.save(tag);
				trips = this.tripService.findTripsByTagId(tag.getId());
				Assert.isTrue(trips.isEmpty());
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(tag, "tag.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Tag tag, final BindingResult binding) {
		ModelAndView result;

		try {
			this.tagService.delete(tag);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(tag, "tag.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Tag tag) {
		ModelAndView result;

		result = this.createEditModelAndView(tag, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Tag tag, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("tag/edit");
		result.addObject("tag", tag);

		result.addObject("message", messageCode);

		return result;

	}
}
