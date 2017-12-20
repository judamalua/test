
package controllers.explorer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ExplorerService;
import services.SurvivalClassService;
import controllers.AbstractController;
import domain.Explorer;
import domain.SurvivalClass;

@Controller
@RequestMapping("/survivalClass/explorer")
public class SurvivalClassExplorerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService			actorService;

	@Autowired
	ExplorerService			explorerService;

	@Autowired
	SurvivalClassService	survivalClassService;


	// Constructors -----------------------------------------------------------

	public SurvivalClassExplorerController() {
		super();
	}

	// Listing ------------------------------------------------------------------

	@RequestMapping(value = "/list-joined", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SurvivalClass> survivalClasses;
		final Explorer explorer;

		result = new ModelAndView("survivalClass/list-joined");
		explorer = (Explorer) this.actorService.findActorByPrincipal();

		survivalClasses = this.survivalClassService.findSurvivalClassesByExplorerID(explorer.getId());
		result.addObject("survivalClasses", survivalClasses);

		return result;
	}

	// Join and leave ------------------------------------------------------------------

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(@RequestParam("survivalClassId") final int survivalClassId) {
		ModelAndView result;

		final Explorer explorer;

		explorer = (Explorer) this.actorService.findActorByPrincipal();
		explorer.getSurvivalClasses().add(this.survivalClassService.findOne(survivalClassId));
		this.explorerService.save(explorer);

		result = new ModelAndView("redirect:/survivalClass/explorer/list-joined.do");

		return result;
	}

	@RequestMapping(value = "/leave", method = RequestMethod.GET)
	public ModelAndView leave(@RequestParam("survivalClassId") final int survivalClassId) {

		ModelAndView result;

		final Explorer explorer;

		explorer = (Explorer) this.actorService.findActorByPrincipal();
		explorer.getSurvivalClasses().remove(this.survivalClassService.findOne(survivalClassId));
		this.explorerService.save(explorer);

		result = new ModelAndView("redirect:/survivalClass/explorer/list-joined.do");

		return result;
	}
}
