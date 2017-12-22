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
import java.util.Date;
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
import services.ManagerService;
import services.RejectionService;
import services.SurvivalClassService;
import controllers.AbstractController;
import domain.Manager;
import domain.SurvivalClass;

@Controller
@RequestMapping("/survivalClass/manager")
public class SurvivalClassManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ManagerService			managerService;
	@Autowired
	ActorService			actorService;
	@Autowired
	SurvivalClassService	survivalClassService;
	@Autowired
	RejectionService		rejectionService;


	// Constructors -----------------------------------------------------------

	public SurvivalClassManagerController() {
		super();
	}

	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int survivalClassId) {
		ModelAndView result;
		SurvivalClass survivalClass;
		Manager actor;

		try {
			actor = (Manager) this.actorService.findActorByPrincipal();
			survivalClass = this.survivalClassService.findOne(survivalClassId);
			Assert.notNull(survivalClass);
			Assert.isTrue(actor.getSurvivalClasses().contains(survivalClass));
			Assert.isTrue(survivalClass.getOrganisationMoment().after(new Date()));

			result = this.createEditModelAndView(survivalClass);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final SurvivalClass survivalClass, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(survivalClass, "survivalClass.params.error");
		else
			try {
				Assert.isTrue(survivalClass.getOrganisationMoment().after(new Date()));
				this.survivalClassService.save(survivalClass);
				result = new ModelAndView("redirect:list-managed.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(survivalClass, "survivalClass.commit.error");
			}

		return result;
	}

	// Listing ------------------------------------------------------------------

	@RequestMapping(value = "/list-managed", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SurvivalClass> survivalClasses;
		final Manager manager;
		try {
			result = new ModelAndView("survivalClass/list-managed");
			manager = (Manager) this.actorService.findActorByPrincipal();

			survivalClasses = new HashSet<SurvivalClass>(manager.getSurvivalClasses());
			//		for (final Trip t : manager.getTrips())
			//			survivalClasses.addAll(t.getSurvivalClasses());
			result.addObject("survivalClasses", survivalClasses);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SurvivalClass survivalClass, final BindingResult binding) {
		ModelAndView result;

		try {
			this.survivalClassService.delete(survivalClass);
			result = new ModelAndView("redirect:list-managed.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(survivalClass, "survivalClass.commit.error");
		}

		return result;
	}

	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		SurvivalClass survivalClass;

		survivalClass = this.survivalClassService.create();

		result = this.createEditModelAndView(survivalClass);

		return result;
	}

	// Ancillary methods ---------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final SurvivalClass survivalClass) {
		ModelAndView result;

		result = this.createEditModelAndView(survivalClass, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SurvivalClass survivalClass, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("survivalClass/edit");

		result.addObject("survivalClass", survivalClass);
		result.addObject("message", messageCode);

		return result;
	}

}
