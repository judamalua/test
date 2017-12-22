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

		try {
			application = this.applicationService.findOne(applicationId);
			result = this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final Application application, final BindingResult binding) {
		ModelAndView result;
		Date currentDate;

		if (binding.hasErrors())
			result = this.createEditModelAndView(application, "application.params.error");
		else
			try {
				currentDate = new Date();
				if (!application.getCreditCard().getHolderName().equals("NONE") || !application.getCreditCard().getBrandName().equals("NONE") || !application.getCreditCard().getNumber().equals("0000000000000000")) {
					Assert.isTrue(application.getCreditCard().getExpirationYear() >= currentDate.getYear() % 100);
					if (application.getCreditCard().getExpirationYear() == currentDate.getYear() % 100)
						Assert.isTrue(application.getCreditCard().getExpirationMonth() > currentDate.getMonth() % 100);
					this.applicationService.changeStatus(application, "ACCEPTED");
				}
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
		try {
			application = this.applicationService.findOne(applicationId);

			this.applicationService.changeStatus(application, "CANCELLED");
			this.applicationService.save(application);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Application application, doneApplication;
		Trip trip;
		Explorer explorer;

		try {
			explorer = (Explorer) this.actorService.findActorByPrincipal();
			application = this.applicationService.create();
			trip = this.tripService.findOne(tripId);
			doneApplication = this.applicationService.getApplicationTripExplorer(explorer.getId(), trip.getId());
			Assert.isTrue(trip.getPublicationDate().before(new Date()));
			Assert.isTrue(trip.getCancelReason() == null || trip.getCancelReason().equals(""));
			Assert.isTrue(doneApplication == null || !doneApplication.getStatus().equals("ACCEPTED"));
			application.setTrip(trip);
			result = this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

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
