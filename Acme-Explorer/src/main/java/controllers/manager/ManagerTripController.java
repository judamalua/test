
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.LegalTextService;
import services.ManagerService;
import services.RangerService;
import services.TagService;
import services.TripService;
import controllers.AbstractController;
import domain.Category;
import domain.LegalText;
import domain.Manager;
import domain.Ranger;
import domain.Tag;
import domain.Trip;

@Controller
@RequestMapping("/trip/manager")
public class ManagerTripController extends AbstractController {

	@Autowired
	TripService			tripService;

	@Autowired
	ActorService		actorService;

	@Autowired
	ManagerService		managerService;

	@Autowired
	RangerService		rangerService;

	@Autowired
	LegalTextService	legalTextService;

	@Autowired
	TagService			tagService;

	@Autowired
	CategoryService		categoryService;


	// Constructors -----------------------------------------------------------

	public ManagerTripController() {
		super();
	}

	// Listing ------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Trip> trips;
		final Manager manager;

		result = new ModelAndView("messageFolder/list");
		manager = (Manager) this.actorService.findActorByPrincipal();

		trips = this.managerService.findTripsByManager(manager.getId());
		result.addObject("trips", trips);

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tripId) {
		ModelAndView result;
		Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		result = this.createEditModelAndView(trip);

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Trip trip;

		trip = this.tripService.create();
		result = this.createEditModelAndView(trip);

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Trip trip) {
		ModelAndView result;

		result = this.createEditModelAndView(trip, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Trip trip, final String messageCode) {
		ModelAndView result;
		Collection<Ranger> rangers;
		final Collection<LegalText> legalTexts;
		final Collection<Tag> tags;
		final Collection<Category> categories;

		rangers = this.rangerService.findAll();
		legalTexts = this.legalTextService.findAll();
		tags = this.tagService.findAll();
		categories = this.categoryService.findAll();

		result = new ModelAndView("trip/edit");
		result.addObject("rangers", rangers);
		result.addObject("legalTexts", legalTexts);
		result.addObject("tags", tags);
		result.addObject("categories", categories);

		result.addObject("message", messageCode);

		return result;

	}
}
