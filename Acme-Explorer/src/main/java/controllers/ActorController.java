/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ExplorerService;
import services.RangerService;
import services.UserAccountService;
import domain.Actor;
import domain.Explorer;
import domain.Ranger;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	ActorService		actorService;
	@Autowired
	ExplorerService		explorerService;
	@Autowired
	RangerService		rangerService;
	@Autowired
	UserAccountService	userAccountService;


	// Constructors -----------------------------------------------------------

	public ActorController() {
		super();
	}

	// Register-Explorer ---------------------------------------------------------------		

	@RequestMapping(value = "/register-explorer", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		Actor actor;

		actor = this.explorerService.create();

		result = this.createEditModelAndView(actor);

		return result;
	}

	@RequestMapping(value = "/register-explorer", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@Valid final Explorer explorer, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(explorer, "actor.params.error");
		else
			try {
				this.actorService.registerExplorer(explorer);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(explorer, "actor.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/register-ranger", method = RequestMethod.GET)
	public ModelAndView registerRanger() {
		ModelAndView result;
		Actor actor;

		actor = this.rangerService.create();

		result = this.createEditModelAndView(actor);

		return result;
	}

	// Register-ranger -----------------------------------------------

	@RequestMapping(value = "/register-ranger", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@Valid final Ranger ranger, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(ranger, "actor.params.error");
		else
			try {
				this.actorService.registerRanger(ranger);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(ranger, "actor.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.createEditModelAndView(actor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;

		if (actor instanceof Ranger) {
			result = new ModelAndView("actor/register-ranger");
			result.addObject("authority", Authority.RANGER);
		} else {
			result = new ModelAndView("actor/register-explorer");
			result.addObject("authority", Authority.EXPLORER);
		}

		result.addObject("actor", actor);
		result.addObject("message", messageCode);

		return result;
	}
}
