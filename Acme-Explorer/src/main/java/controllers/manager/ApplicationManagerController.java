/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.manager;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.ApplicationService;
import services.ManagerService;
import services.RejectionService;
import controllers.AbstractController;
import domain.Application;
import domain.Manager;
import domain.Rejection;

@Controller
@RequestMapping("/application/manager")
public class ApplicationManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ManagerService		managerService;
	@Autowired
	ActorService		actorService;
	@Autowired
	ApplicationService	applicationService;
	@Autowired
	RejectionService	rejectionService;


	// Constructors -----------------------------------------------------------

	public ApplicationManagerController() {
		super();
	}

	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("application/list");

		final UserAccount userAccount = LoginService.getPrincipal();
		final Manager actor = (Manager) this.actorService.findActorByUserAccountId(userAccount.getId());

		final Collection<Application> applications = this.managerService.findManagedApplicationsByManager(actor);
		result.addObject("applications", applications);
		result.addObject("requestUri", "application/manager/list.do");

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
			result = new ModelAndView("redirect:/misc/index.do");
		}

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final Application application, final BindingResult binding) {
		ModelAndView result;
		Rejection rejection;

		if (binding.hasErrors())
			result = this.createEditModelAndView(application, "application.params.error");
		else
			try {
				this.applicationService.changeStatus(application, "REJECTED");
				rejection = application.getRejection();
				rejection.setApplication(application);
				this.rejectionService.save(rejection);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(application, "application.commit.error");
			}

		return result;
	}
	// Changing status -------------------------------------------------------------------
	@RequestMapping(value = "/change-status", method = RequestMethod.GET)
	public ModelAndView save(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			application = this.applicationService.findOne(applicationId);
			this.applicationService.changeStatus(application, "DUE");
			this.applicationService.save(application);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/index.do");
		}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Application application, final BindingResult binding) {
		ModelAndView result;

		try {
			this.applicationService.delete(application);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}

		return result;
	}

	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Application application;

		application = this.applicationService.create();

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
