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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SurvivalClassService;
import controllers.AbstractController;
import domain.Explorer;
import domain.SurvivalClass;

@Controller
@RequestMapping("/survivalClass/explorer")
public class SurvivalClassManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService			actorService;
	@Autowired
	SurvivalClassService	survivalClassService;


	// Constructors -----------------------------------------------------------

	public SurvivalClassManagerController() {
		super();
	}

	// Listing ---------------------------------------------------------------	

	@RequestMapping("/list-joined")
	public ModelAndView list() {
		ModelAndView result;
		Explorer explorer;
		Collection<SurvivalClass> survivalClasses;

		result = new ModelAndView("survivalClass/list-joined");

		explorer = (Explorer) this.actorService.findActorByPrincipal();

		survivalClasses = this.survivalClassService.findSurvivalClassesByExplorerID(explorer.getId());
		result.addObject("survivalClasses", survivalClasses);
		result.addObject("requestUri", "survivalClass/explorer/list-joined.do");

		return result;
	}

	// leaving ---------------------------------------------------------------	
	@RequestMapping(value = "/leave", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int survivalClassId) {
		ModelAndView result;
		SurvivalClass survivalClass;
		Explorer explorer;

		survivalClass = this.survivalClassService.findOne(survivalClassId);
		explorer = (Explorer) this.actorService.findActorByPrincipal();
		explorer.getSurvivalClasses().remove(survivalClass);
		this.actorService.save(explorer);

		result = new ModelAndView("redirect:list.do");

		return result;
	}

}
