
package controllers.explorer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
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
	SurvivalClassService	survivalClassService;


	// Constructors -----------------------------------------------------------

	public SurvivalClassExplorerController() {
		super();
	}

	// Listing ------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SurvivalClass> survivalClasses;
		final Explorer explorer;

		result = new ModelAndView("trip/list");
		explorer = (Explorer) this.actorService.findActorByPrincipal();

		survivalClasses = this.survivalClassService.findSurvivalClassesByExplorerID(explorer.getId());
		result.addObject("trips", survivalClasses);

		return result;
	}
}
