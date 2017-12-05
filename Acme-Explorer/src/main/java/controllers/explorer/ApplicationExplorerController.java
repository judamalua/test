/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.explorer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.ExplorerService;
import services.TripService;
import controllers.AbstractController;
import domain.Application;
import domain.Explorer;
import domain.Trip;

@Controller
@RequestMapping("/application/explorer")
public class ApplicationExplorerController extends AbstractController {

	@Autowired
	ExplorerService		explorerService;
	@Autowired
	ActorService		actorService;
	@Autowired
	ApplicationService	applicationService;
	@Autowired
	TripService			tripService;


	// Constructors -----------------------------------------------------------

	public ApplicationExplorerController() {
		super();
	}

	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		Explorer actor;

		result = new ModelAndView("application/list");

		actor = (Explorer) this.actorService.findActorByPrincipal();

		final Collection<Application> applications = this.applicationService.findApplicationsGroupByStatus(actor);
		result.addObject("applications", applications);
		result.addObject("requestUri", "application/explorer/list.do");

		return result;
	}
	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		result = this.createEditModelAndView(application);

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final Application application, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(application, "application.params.error");
		else
			try {
				this.applicationService.save(application);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(application, "application.commit.error");
			}

		return result;
	}

	//	// Deleting ------------------------------------------------------------------------
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(final Application application, final BindingResult binding) {
	//		ModelAndView result;
	//
	//		try {
	//			this.applicationService.delete(application);
	//			result = new ModelAndView("redirect:list.do");
	//
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(application, "messageFolder.commit.error");
	//		}
	//
	//		return result;
	//	}

	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		final ModelAndView result;
		Application application;
		Trip trip;

		application = this.applicationService.create();
		trip = this.tripService.findOne(tripId);
		application.setTrip(trip);

		result = this.createEditModelAndView(application);

		return result;
	}

	// Ancillary methods ---------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/edit");

		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

}
