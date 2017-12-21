
package controllers.manager;

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
import services.StageService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
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
	@Autowired
	ActorService	actorService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int stageId) {
		ModelAndView result;
		Stage stage;
		Trip trip;
		Manager manager;

		try {
			manager = (Manager) this.actorService.findActorByPrincipal();
			stage = this.stageService.findOne(stageId);
			Assert.notNull(stage);
			trip = this.tripService.getTripFromStageId(stageId);
			Assert.isTrue(trip.getManagers().contains(manager));
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			result = this.createEditModelAndView(stage, trip.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Stage stage;
		Trip trip;
		try {
			stage = this.stageService.create();
			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			result = this.createEditModelAndView(stage, tripId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam("trip") final int tripId, @ModelAttribute("stage") @Valid final Stage stage, final BindingResult binding) {
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
