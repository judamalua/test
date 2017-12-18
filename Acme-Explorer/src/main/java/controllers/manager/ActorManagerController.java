
package controllers.manager;

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
import domain.Manager;

@Controller
@RequestMapping("/actor/manager")
public class ActorManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Manager manager;

		manager = (Manager) this.actorService.findActorByPrincipal();
		Assert.notNull(manager);
		result = this.createEditModelAndView(manager);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") @Valid final Manager manager, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(manager, "actor.params.error");
		else
			try {
				this.actorService.save(manager);
				result = new ModelAndView("redirect:/welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(manager, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Manager manager) {
		ModelAndView result;

		result = this.createEditModelAndView(manager, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Manager manager, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", manager);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/manager/edit.do");

		return result;

	}

}
