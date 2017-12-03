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
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
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

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping(value = "/register-explorer", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		Actor actor;
		UserAccount userAccount, savedUserAccount;
		Authority authority;

		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority(Authority.EXPLORER);
		userAccount.addAuthority(authority);
		savedUserAccount = this.userAccountService.save(userAccount);

		actor = this.explorerService.create();
		actor.setUserAccount(savedUserAccount);

		result = this.createEditModelAndView(actor);

		return result;
	}

	@RequestMapping(value = "/register-explorer", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@Valid final Explorer explorer, final BindingResult binding) {
		ModelAndView result;
		String password;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();

		password = explorer.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);
		explorer.getUserAccount().setPassword(password);

		if (binding.hasErrors())
			result = this.createEditModelAndView(explorer, "actor.params.error");
		else
			try {

				this.actorService.registerExplorer(explorer);
				result = new ModelAndView("redirect: /");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(explorer, "actor.commit.error");
			}
		result = this.createEditModelAndView(explorer);

		return result;
	}
	@RequestMapping(value = "/register-ranger", method = RequestMethod.GET)
	public ModelAndView registerRanger() {
		ModelAndView result;
		Actor actor;
		UserAccount userAccount;
		Authority authority;

		userAccount = new UserAccount();
		authority = new Authority();
		authority.setAuthority(Authority.RANGER);
		userAccount.addAuthority(authority);

		actor = this.rangerService.create();
		actor.setUserAccount(userAccount);

		result = this.createEditModelAndView(actor);

		return result;
	}

	@RequestMapping(value = "/register-ranger", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorer(@Valid final Ranger ranger, final BindingResult binding) {
		ModelAndView result;
		String password;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();

		password = ranger.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);

		ranger.getUserAccount().setPassword(password);
		this.userAccountService.save(ranger.getUserAccount());

		if (binding.hasErrors())
			result = this.createEditModelAndView(ranger, "actor.params.error");
		else
			try {
				this.actorService.registerRanger(ranger);
				result = new ModelAndView("redirect: /");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(ranger, "actor.commit.error");
			}
		result = this.createEditModelAndView(ranger);

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
