
package controllers.explorer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.ExplorerService;
import services.LegalTextService;
import services.SurvivalClassService;
import services.TagService;
import services.TripService;
import controllers.AbstractController;
import domain.Explorer;
import domain.SurvivalClass;

@Controller
@RequestMapping("/survivalClass/explorer")
public class SurvivalClassExplorerController extends AbstractController {

	@Autowired
	TripService				tripService;

	@Autowired
	ActorService			actorService;

	@Autowired
	ExplorerService			explorerService;

	@Autowired
	SurvivalClassService	survivalClassService;

	@Autowired
	LegalTextService		legalTextService;

	@Autowired
	TagService				tagService;

	@Autowired
	CategoryService			categoryService;


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
	//	// Editing ---------------------------------------------------------
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int tripId) {
	//		ModelAndView result;
	//		SurvivalClass trip;
	//
	//		trip = this.tripService.findOne(tripId);
	//		Assert.notNull(trip);
	//		result = this.createEditModelAndView(trip);
	//
	//		return result;
	//	}
	//
	//	// Creating ---------------------------------------------------------
	//
	//	@RequestMapping(value = "/create", method = RequestMethod.GET)
	//	public ModelAndView create() {
	//		ModelAndView result;
	//		survivalClass trip;
	//
	//		trip = this.tripService.create();
	//		result = this.createEditModelAndView(trip);
	//
	//		return result;
	//	}
	//
	//	// Ancillary methods --------------------------------------------------
	//
	//	protected ModelAndView createEditModelAndView(final survivalClass trip) {
	//		ModelAndView result;
	//
	//		result = this.createEditModelAndView(trip, null);
	//
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(final survivalClass trip, final String messageCode) {
	//		ModelAndView result;
	//		Collection<Ranger> rangers;
	//		final Collection<LegalText> legalTexts;
	//		final Collection<Tag> tags;
	//		final Collection<Category> categories;
	//
	//		rangers = this.rangerService.findAll();
	//		legalTexts = this.legalTextService.findAll();
	//		tags = this.tagService.findAll();
	//		categories = this.categoryService.findAll();
	//
	//		result = new ModelAndView("trip/edit");
	//		result.addObject("rangers", rangers);
	//		result.addObject("legalTexts", legalTexts);
	//		result.addObject("tags", tags);
	//		result.addObject("categories", categories);
	//
	//		result.addObject("message", messageCode);
	//
	//		return result;
	//
	//	}
}
