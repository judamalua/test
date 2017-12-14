/*
 * AbstractController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;

@Controller
public class AbstractController {

	@Autowired
	private ConfigurationService	configurationService;


	/**
	 * 
	 * @return the number of result to be shown in every list table, stored in the database
	 * 
	 * @author Juanmi
	 */
	@ModelAttribute(value = "pagesize")
	//@RequestMapping(method = RequestMethod.POST)
	public Integer pagesize() {
		Integer result;

		result = this.configurationService.findConfiguration().getMaxResults();

		return result;
	}

	/**
	 * 
	 * @return banner URL of the system as a model attribute to be used in any other view
	 * 
	 * @author Juanmi
	 */

	@ModelAttribute(value = "banner")
	//@RequestMapping(method = RequestMethod.POST)
	public String banner() {
		String result;

		result = this.configurationService.getBannerUrl();

		return result;
	}

	/**
	 * 
	 * @return english version of the welcome message stored in configuration entity
	 * 
	 * @author Juanmi
	 */
	@ModelAttribute(value = "welcomeMessageEng")
	//@RequestMapping(method = RequestMethod.POST)
	public String welcomeMessageEng() {
		String result;

		result = this.configurationService.getWelcomeMessageEng();

		return result;
	}

	/**
	 * 
	 * @return spanish version of the welcome message stored in configuration entity
	 * 
	 * @author Juanmi
	 */
	@ModelAttribute(value = "welcomeMessageEsp")
	//@RequestMapping(value = "*", method = RequestMethod.POST)
	//@RequestMapping(method = RequestMethod.POST)
	public String welcomeMessageEsp() {
		String result;

		result = this.configurationService.getWelcomeMessageEsp();

		return result;
	}

	// Panic handler ----------------------------------------------------------
	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}
