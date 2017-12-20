
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

import security.Authority;
import services.ActorService;
import services.AdministratorService;
import services.AuditorService;
import services.ExplorerService;
import services.ManagerService;
import services.RangerService;
import services.SponsorService;
import services.UserAccountService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Auditor;
import domain.Explorer;
import domain.Manager;
import domain.Ranger;
import domain.Sponsor;

@Controller
@RequestMapping("/actor/admin")
public class ActorAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService			actorService;
	@Autowired
	ManagerService			managerService;
	@Autowired
	AdministratorService	administratorService;
	@Autowired
	SponsorService			sponsorService;
	@Autowired
	AuditorService			auditorService;
	@Autowired
	RangerService			rangerService;
	@Autowired
	ExplorerService			explorerService;
	@Autowired
	UserAccountService		userAccountService;


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

	@RequestMapping("/register")
	public ModelAndView register() {
		ModelAndView result;

		result = new ModelAndView("actor/register");
		return result;
	}

	// Registering manager ------------------------------------------------------------
	@RequestMapping(value = "/register-manager", method = RequestMethod.GET)
	public ModelAndView registerManager() {
		ModelAndView result;
		Actor actor;

		actor = this.managerService.create();

		result = this.createEditModelAndViewRegister(actor);
		return result;
	}

	//Saving manager ---------------------------------------------------------------------
	@RequestMapping(value = "/register-manager", method = RequestMethod.POST, params = "save")
	public ModelAndView registerManagerSave(@ModelAttribute("actor") @Valid final Manager actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.MANAGER);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
			}

		return result;
	}

	// Registering admin ------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView result;
		Actor actor;

		actor = this.administratorService.create();

		result = this.createEditModelAndViewRegister(actor);
		return result;
	}

	//Saving admin ---------------------------------------------------------------------
	@RequestMapping(value = "/register-admin", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAdminSave(@ModelAttribute("actor") @Valid final Administrator actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.ADMIN);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
			}

		return result;
	}

	// Registering auditor ------------------------------------------------------------
	@RequestMapping(value = "/register-auditor", method = RequestMethod.GET)
	public ModelAndView registerAuditor() {
		ModelAndView result;
		Actor actor;

		actor = this.auditorService.create();

		result = this.createEditModelAndViewRegister(actor);
		return result;
	}

	//Saving auditor ---------------------------------------------------------------------
	@RequestMapping(value = "/register-auditor", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAuditorSave(@ModelAttribute("actor") @Valid final Auditor actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.AUDITOR);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
			}

		return result;
	}
	// Registering sponsor ------------------------------------------------------------
	@RequestMapping(value = "/register-sponsor", method = RequestMethod.GET)
	public ModelAndView registerSponsor() {
		ModelAndView result;
		Actor actor;

		actor = this.sponsorService.create();

		result = this.createEditModelAndViewRegister(actor);
		return result;
	}

	//Saving sponsor ---------------------------------------------------------------------
	@RequestMapping(value = "/register-sponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView registerSponsorSave(@ModelAttribute("actor") @Valid final Sponsor actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.SPONSOR);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
			}

		return result;
	}

	// Registering ranger ------------------------------------------------------------
	@RequestMapping(value = "/register-ranger", method = RequestMethod.GET)
	public ModelAndView registerRanger() {
		ModelAndView result;
		Actor actor;

		actor = this.rangerService.create();

		result = this.createEditModelAndViewRegister(actor);
		result.addObject("actionURL", "actor/admin/register-ranger.do");
		return result;

	}

	//Saving ranger ---------------------------------------------------------------------
	@RequestMapping(value = "/register-ranger", method = RequestMethod.POST, params = "save")
	public ModelAndView registerRangerSave(@ModelAttribute("actor") @Valid final Ranger actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.RANGER);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
			}

		return result;
	}

	// Registering explorer ------------------------------------------------------------
	@RequestMapping(value = "/register-explorer", method = RequestMethod.GET)
	public ModelAndView registerExplorer() {
		ModelAndView result;
		Actor actor;

		actor = this.explorerService.create();

		result = this.createEditModelAndViewRegister(actor);
		result.addObject("actionURL", "actor/admin/register-explorer.do");
		return result;
	}

	//Saving explorer ---------------------------------------------------------------------
	@RequestMapping(value = "/register-explorer", method = RequestMethod.POST, params = "save")
	public ModelAndView registerExplorerSave(@ModelAttribute("actor") @Valid final Explorer actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "actor.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.EXPLORER);
				Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));
				this.actorService.registerActor(actor);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegister(actor, "actor.commit.error");
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

	protected ModelAndView createEditModelAndViewRegister(final Actor actor) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(actor, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegister(final Actor actor, final String messageCode) {
		ModelAndView result = null;

		if (actor instanceof Ranger)
			result = this.createEditModelAndViewRegister(actor, Authority.RANGER, messageCode);
		if (actor instanceof Manager)
			result = this.createEditModelAndViewRegister(actor, Authority.MANAGER, messageCode);
		if (actor instanceof Administrator)
			result = this.createEditModelAndViewRegister(actor, Authority.ADMIN, messageCode);
		if (actor instanceof Auditor)
			result = this.createEditModelAndViewRegister(actor, Authority.AUDITOR, messageCode);
		if (actor instanceof Sponsor)
			result = this.createEditModelAndViewRegister(actor, Authority.SPONSOR, messageCode);
		if (actor instanceof Explorer)
			result = this.createEditModelAndViewRegister(actor, Authority.EXPLORER, messageCode);

		return result;
	}

	protected final ModelAndView createEditModelAndViewRegister(final Actor actor, final String authority, final String messageCode) {
		final ModelAndView result;
		String url;
		switch (authority) {
		case "RANGER":
			url = "actor/register-ranger";
			break;
		case "MANAGER":
			url = "actor/register-manager";
			break;
		case "ADMIN":
			url = "actor/register-admin";
			break;
		case "AUDITOR":
			url = "actor/register-auditor";
			break;
		case "SPONSOR":
			url = "actor/register-sponsor";
			break;
		default:
			url = "actor/register-explorer";
			break;
		}
		result = new ModelAndView(url);
		result.addObject("authority", authority);
		result.addObject("requestUri", url + ".do");
		result.addObject("actor", actor);
		result.addObject("message", messageCode);
		return result;
	}
}
