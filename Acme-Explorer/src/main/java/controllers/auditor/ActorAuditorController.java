
package controllers.auditor;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Auditor;

@Controller
@RequestMapping("/actor/auditor")
public class ActorAuditorController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Auditor auditor;

		auditor = (Auditor) this.actorService.findActorByPrincipal();
		Assert.notNull(auditor);
		result = this.createEditModelAndView(auditor);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final Auditor auditor, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(auditor, "actor.params.error");
		else
			try {
				this.actorService.save(auditor);
				result = new ModelAndView("redirect:/welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(auditor, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Auditor auditor) {
		ModelAndView result;

		result = this.createEditModelAndView(auditor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Auditor auditor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", auditor);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/auditor/edit.do");

		return result;

	}

}
