
package controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Administrator;

@Controller
@RequestMapping("/actor/admin")
public class ActorAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Administrator administrator;

		administrator = (Administrator) this.actorService.findActorByPrincipal();
		Assert.notNull(administrator);
		result = this.createEditModelAndView(administrator);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Administrator administrator, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(administrator, "actor.params.error");
		else
			try {
				this.actorService.save(administrator);
				result = new ModelAndView("redirect:/welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(administrator, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Administrator administrator) {
		ModelAndView result;

		result = this.createEditModelAndView(administrator, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Administrator administrator, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");

		result.addObject("actor", administrator);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "actor/admin/edit.do");

		return result;

	}
}
