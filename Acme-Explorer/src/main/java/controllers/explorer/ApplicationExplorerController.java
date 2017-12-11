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

	// Services -------------------------------------------------------

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
		Explorer explorer;
		Collection<Application> applications;

		result = new ModelAndView("application/list");

		explorer = (Explorer) this.actorService.findActorByPrincipal();

		applications = this.applicationService.findApplications(explorer);
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
				if (!application.getCreditCard().getHolderName().equals("NONE") || !application.getCreditCard().getBrandName().equals("NONE") || !application.getCreditCard().getNumber().equals("0000000000000000"))
					this.applicationService.changeStatus(application, "ACCEPTED");
				this.applicationService.save(application);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(application, "application.commit.error");
			}

		return result;
	}

	//	Cancelling ------------------------------------------------------------------------

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		this.applicationService.changeStatus(application, "CANCELLED");
		this.applicationService.save(application);
		result = new ModelAndView("redirect:list.do");

		return result;
	}
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
