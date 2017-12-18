
package controllers.admin;

import java.util.Collection;

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
import services.ManagerService;
import services.UserAccountService;

import com.mchange.v1.cachedstore.CachedStore.Manager;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;

@Controller
@RequestMapping("/actor/admin")
public class ActorAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService		actorService;
	@Autowired
	ManagerService		managerService;
	@Autowired
	UserAccountService	userAccountService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list-suspicious")
	public ModelAndView listSuspicious() {
		ModelAndView result;

		result = new ModelAndView("actor/list-suspicious");

		final Collection<Actor> actors = this.actorService.findSuspicious();
		result.addObject("actors", actors);
		result.addObject("requestUri", "actor/admin/list-suspicious.do");

		return result;
	}
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
	public ModelAndView save(@ModelAttribute("actor") @Valid final Administrator administrator, final BindingResult binding) {
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

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(final int actorId) {
		ModelAndView result;

		try {
			final Actor actor = this.actorService.findOne(actorId);

			Assert.notNull(actor);
			Assert.isTrue(!actor.getUserAccount().getBanned());

			this.userAccountService.ban(actor);

			result = this.listSuspicious();
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(final int actorId) {
		ModelAndView result;

		try {
			final Actor actor = this.actorService.findOne(actorId);

			Assert.notNull(actor);
			Assert.isTrue(actor.getUserAccount().getBanned());

			this.userAccountService.unban(actor);

			result = this.listSuspicious();
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Registering explorer ------------------------------------------------------------
	@RequestMapping(value = "/register-manager", method = RequestMethod.GET)
	public ModelAndView registerManager() {
		ModelAndView result;
		Actor actor;

		actor = this.managerService.create();

		result = new ModelAndView("actor/register-manager");

		result.addObject("actor", actor);
		result.addObject("message", null);
		result.addObject("requestUri", "actor/admin/edit.do");
		return result;
	}

	//Saving manager ---------------------------------------------------------------------
	@RequestMapping(value = "/register-manager", method = RequestMethod.POST, params = "save")
	public ModelAndView registerManagerSave(@Valid final Manager manager, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("actor/register-manager");

			result.addObject("actor", manager);
			result.addObject("message", "actor.params.error");
			result.addObject("requestUri", "actor/admin/edit.do");
		} else
			try {
				this.actorService.registerActor((Actor) manager);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("actor/register-manager");

				result.addObject("actor", manager);
				result.addObject("message", "actor.commit.error");
				result.addObject("requestUri", "actor/admin/edit.do");
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
