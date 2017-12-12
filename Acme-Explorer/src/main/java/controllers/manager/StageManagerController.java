
package controllers.manager;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.StageService;
import services.TripService;
import controllers.AbstractController;
import domain.Stage;
import domain.Trip;

@Controller
@RequestMapping("/stage/manager")
public class StageManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	StageService	stageService;
	@Autowired
	TripService		tripService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int stageId) {
		ModelAndView result;
		Stage stage;
		Trip trip;

		stage = this.stageService.findOne(stageId);
		Assert.notNull(stage);
		trip = this.tripService.getTripFromStageId(stageId);

		result = this.createEditModelAndView(stage, trip.getId());

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Stage stage;

		stage = this.stageService.create();

		result = this.createEditModelAndView(stage, tripId);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Stage stage, @RequestParam("trip") final int tripId, final BindingResult binding) {
		ModelAndView result;
		Trip trip;

		if (binding.hasErrors())
			result = this.createEditModelAndView(stage, tripId, "stage.params.error");
		else
			try {
				trip = this.tripService.findOne(tripId);
				trip.getStages().add(stage);
				this.tripService.save(trip);
				result = new ModelAndView("redirect:/trip/manager/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(stage, tripId, "stage.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Stage stage, final BindingResult binding) {
		ModelAndView result;
		Trip trip;
		trip = this.tripService.getTripFromStageId(stage.getId());

		try {
			trip.getStages().remove(stage);
			this.tripService.save(trip);
			result = new ModelAndView("redirect:/trip/manager/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(stage, trip.getId(), "category.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Stage stage, final int tripId) {
		ModelAndView result;

		result = this.createEditModelAndView(stage, tripId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Stage stage, final int tripId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("stage/edit");
		result.addObject("stage", stage);
		result.addObject("trip", tripId);

		result.addObject("message", messageCode);

		return result;

	}
}
