
package controllers.explorer;

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
import domain.Explorer;

@Controller
@RequestMapping("/actor/explorer")
public class ActorExplorerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Explorer explorer;

		explorer = (Explorer) this.actorService.findActorByPrincipal();
		Assert.notNull(explorer);
		result = this.createEditModelAndView(explorer);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("explorer") @Valid final Explorer explorer, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(explorer, "actor.params.error");
		else
			try {
				this.actorService.save(explorer);
				result = new ModelAndView("redirect:/welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(explorer, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Explorer explorer) {
		ModelAndView result;

		result = this.createEditModelAndView(explorer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Explorer explorer, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", explorer);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/explorer/edit.do");

		return result;

	}

}
