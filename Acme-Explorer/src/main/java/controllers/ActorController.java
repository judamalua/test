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
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	// Services -------------------------------------------------------
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

	// Editing ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);

		return result;
	}

	//Saving --------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final Actor actor, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("actor/edit");
			result.addObject("actor", actor);
			result.addObject("message", "actor.params.error");
		} else
			try {
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("actor/edit");
				result.addObject("actor", actor);
				result.addObject("message", "actor.params.error");
			}

		return result;
	}

	// Registering explorer ------------------------------------------------------------
	@RequestMapping(value = "/register-explorer", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		Actor actor;

		actor = this.explorerService.create();

		result = this.createEditModelAndView(actor);

		result.addObject("actionURL", "actor/register-explorer.do");

		return result;
	}

	//Saving explorer ---------------------------------------------------------------------
	@RequestMapping(value = "/register-explorer", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@ModelAttribute("actor") @Valid final Explorer explorer, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(explorer, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.EXPLORER);
				Assert.isTrue(explorer.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerExplorer(explorer);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(explorer, "actor.commit.error");
			}

		return result;
	}
	// Registering ranger ----------------------------------------------------------------
	@RequestMapping(value = "/register-ranger", method = RequestMethod.GET)
	public ModelAndView registerRanger() {
		ModelAndView result;
		Actor actor;

		actor = this.rangerService.create();

		result = this.createEditModelAndView(actor);

		result.addObject("actionURL", "actor/register-explorer.do");

		return result;
	}

	// Saving ranger -----------------------------------------------

	@RequestMapping(value = "/register-ranger", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@ModelAttribute("actor") @Valid final Ranger ranger, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndView(ranger, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.RANGER);
				Assert.isTrue(ranger.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerRanger(ranger);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(ranger, "actor.commit.error");
			}

		return result;
	}

	//Ancillary methods ----------------------------------------------------------------

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
			result.addObject("requestUri", "actor/register-ranger.do");
		} else {
			result = new ModelAndView("actor/register-explorer");
			result.addObject("authority", Authority.EXPLORER);
			result.addObject("requestUri", "actor/register-explorer.do");
		}

		result.addObject("actor", actor);
		result.addObject("message", messageCode);

		return result;
	}
}
