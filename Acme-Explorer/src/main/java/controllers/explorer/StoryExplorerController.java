
package controllers.explorer;

import java.util.Arrays;
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

import services.ActorService;
import services.StoryService;
import services.TripService;
import controllers.AbstractController;
import domain.Story;
import domain.Trip;

@Controller
@RequestMapping("/story/explorer")
public class StoryExplorerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;

	@Autowired
	StoryService	storyService;
	@Autowired
	TripService		tripService;


	// Constructors -----------------------------------------------------------

	public StoryExplorerController() {
		super();
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("storyId") final int storyId, @RequestParam("tripId") final int tripId) {
		ModelAndView result;
		Story story;
		Trip trip;

		try {
			story = this.storyService.findOne(storyId);
			Assert.notNull(story);
			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getEndDate().before(new Date()));
			result = this.createEditModelAndView(story, tripId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Story story;

		story = this.storyService.create();

		result = this.createEditModelAndView(story, tripId);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam("trip") final int tripId, @Valid final Story story, final BindingResult binding) {
		ModelAndView result;
		Trip trip;
		//Los attachments se separan por comas para introducir varios valores
		if (story.getAttachments().size() == 1)
			story.setAttachments(Arrays.asList(story.getAttachments().iterator().next().split(",")));
		if (binding.hasErrors())
			result = this.createEditModelAndView(story, tripId, "story.params.error");
		else
			try {
				trip = this.tripService.findOne(tripId);
				this.storyService.save(story, trip);
				result = new ModelAndView("redirect:/trip/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(story, tripId, "story.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Story story, final BindingResult binding) {
		ModelAndView result;
		Trip trip;
		trip = this.tripService.getTripFromStory(story.getId());

		try {
			trip.getStories().remove(story);
			this.tripService.save(trip);
			result = new ModelAndView("redirect:/trip/explorer/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(story, trip.getId(), "category.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Story story, final int tripId) {
		ModelAndView result;

		result = this.createEditModelAndView(story, tripId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Story story, final int tripId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("story/edit");
		result.addObject("story", story);
		result.addObject("trip", tripId);

		result.addObject("message", messageCode);

		return result;

	}

}
