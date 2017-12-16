
package controllers.admin;

import java.util.Collection;

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
import services.ConfigurationService;
import services.LegalTextService;
import controllers.AbstractController;
import domain.LegalText;

@Controller
@RequestMapping("/legalText/admin")
public class LegalTextAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	LegalTextService		legalTextService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;


	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<LegalText> legalTexts;

		result = new ModelAndView("legalText/list");

		legalTexts = this.legalTextService.findAll();
		result.addObject("legalTexts", legalTexts);

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalTextId) {
		ModelAndView result;
		LegalText legalText;

		try {
			legalText = this.legalTextService.findOne(legalTextId);
			Assert.notNull(legalText);
			Assert.isTrue(!legalText.getFinalMode());
			result = this.createEditModelAndView(legalText);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalText legalText;

		legalText = this.legalTextService.create();
		result = this.createEditModelAndView(legalText);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@Valid final LegalText legalText, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalText, "legalText.params.error");
		else
			try {
				this.legalTextService.save(legalText);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalText, "legalText.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@Valid final LegalText legalText, final BindingResult binding) {
		ModelAndView result;
		legalText.setFinalMode(true);

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalText, "legalText.params.error");
		else
			try {
				this.legalTextService.save(legalText);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalText, "legalText.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalText legalText, final BindingResult binding) {
		ModelAndView result;

		try {
			Assert.isTrue(!legalText.getFinalMode());
			this.legalTextService.delete(legalText);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(legalText, "legalText.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final LegalText legalText) {
		ModelAndView result;

		result = this.createEditModelAndView(legalText, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalText legalText, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("legalText/edit");
		result.addObject("legalText", legalText);

		result.addObject("message", messageCode);

		return result;

	}
}
