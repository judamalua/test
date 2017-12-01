/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.ApplicationService;
import services.ManagerService;
import domain.Application;
import domain.Manager;

@Controller
@RequestMapping("/manager")
public class ApplicationController extends AbstractController {

	@Autowired
	ManagerService		managerService;
	@Autowired
	ActorService		actorService;
	@Autowired
	ApplicationService	applicationService;


	// Constructors -----------------------------------------------------------

	public ApplicationController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("application/list");

		final UserAccount userAccount = LoginService.getPrincipal();
		final Manager actor = (Manager) this.actorService.findActorByUserAccountId(userAccount.getId());

		final Collection<Application> applications = this.managerService.findManagedApplicationsByManager(actor);
		result.addObject("applications", applications);

		return result;
	}
	// Action-2 ---------------------------------------------------------------		

	@RequestMapping("/action-2")
	public ModelAndView action2() {
		ModelAndView result;

		result = new ModelAndView("customer/action-2");

		return result;
	}
}
