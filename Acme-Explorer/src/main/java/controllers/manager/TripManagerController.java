
package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.LegalTextService;
import services.ManagerService;
import services.RangerService;
import services.StageService;
import services.SurvivalClassService;
import services.TagService;
import services.TripService;
import controllers.AbstractController;
import domain.Category;
import domain.LegalText;
import domain.Manager;
import domain.Ranger;
import domain.Stage;
import domain.SurvivalClass;
import domain.Tag;
import domain.Trip;

@Controller
@RequestMapping("/trip/manager")
public class TripManagerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	TripService				tripService;

	@Autowired
	ActorService			actorService;

	@Autowired
	ManagerService			managerService;

	@Autowired
	RangerService			rangerService;

	@Autowired
	LegalTextService		legalTextService;

	@Autowired
	TagService				tagService;

	@Autowired
	CategoryService			categoryService;

	@Autowired
	SurvivalClassService	survivalClassService;
	@Autowired
	StageService			stageService;


	// Constructors -----------------------------------------------------------

	public TripManagerController() {
		super();
	}

	// Listing ------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Trip> trips;
		final Manager manager;

		result = new ModelAndView("trip/list");
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
	// joining ---------------------------------------------------------------------------
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(@RequestParam final int tripId) {
		ModelAndView result;
		Trip trip;
		Manager manager;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		manager = (Manager) this.actorService.findActorByPrincipal();
		trip.getManagers().add(manager);
		this.tripService.save(trip);
		result = new ModelAndView("trip/list");

		return result;
	}

	// Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam("tripId") final int tripId) {
		ModelAndView result;
		Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		result = new ModelAndView("trip/cancel-trip");
		result.addObject("trip", tripId);

		return result;
	}

	// Saving Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, params = {
		"save", "reason", "tripId"
	})
	public ModelAndView SaveCancel(final int tripId, final String reason) {
		ModelAndView result;
		Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		trip.setCancelReason(reason);
		this.tripService.save(trip);
		result = new ModelAndView("redirect:list.do");

		return result;
	}
	// Saving -----------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save", "titleStage", "descriptionStage", "priceStage"
	})
	public ModelAndView save(@Valid final Trip trip, @ModelAttribute("titleStage") @NotBlank final String titleStage, @ModelAttribute("descriptionStage") final String descriptionStage, @ModelAttribute("priceStage") @NotBlank final double priceStage,
		final BindingResult binding) {
		ModelAndView result;
		Stage stage;

		if (binding.hasErrors())
			result = this.createEditModelAndView(trip, "trip.params.error");
		else
			try {
				stage = this.stageService.create();
				stage.setTitle(titleStage);
				stage.setPrice(priceStage);
				stage.setDescription(descriptionStage);
				trip.getStages().add(stage);
				this.tripService.save(trip);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(trip, "trip.commit.error");
			}

		return result;
	}

	// Saving -----------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Trip trip, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(trip, "trip.params.error");
		else
			try {
				this.tripService.save(trip);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(trip, "trip.commit.error");
			}

		return result;
	}
	// Deleting -------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Trip trip, final BindingResult binding) {
		ModelAndView result;

		try {
			this.tripService.delete(trip);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(trip, "trip.commit.error");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Trip trip;
		Manager manager;

		trip = this.tripService.create();
		manager = (Manager) this.actorService.findActorByPrincipal();
		trip.getManagers().add(manager);

		result = this.createEditModelAndView(trip);

		return result;
	}

	// Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/manageSurvivalClasses", method = RequestMethod.GET)
	public ModelAndView manageSurvivalClasses(@RequestParam("tripId") final int tripId) {
		ModelAndView result;
		Trip trip;
		final List<SurvivalClass> survivalClasses;
		final List<Boolean> indexedSurvivalClasses;
		final Manager manager;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		result = new ModelAndView("trip/manageSurvivalClasses");
		manager = (Manager) this.actorService.findActorByPrincipal();

		survivalClasses = new ArrayList<SurvivalClass>(trip.getSurvivalClasses());
		indexedSurvivalClasses = new ArrayList<Boolean>();
		survivalClasses.addAll(manager.getSurvivalClasses());
		for (final SurvivalClass sv : survivalClasses)
			indexedSurvivalClasses.add(trip.getSurvivalClasses().contains(sv));
		result.addObject("trip", tripId);
		result.addObject("survivalClasses", survivalClasses);

		result.addObject("indexedSurvivalClasses", indexedSurvivalClasses);

		return result;
	}

	// Saving Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/manageSurvivalClasses", method = RequestMethod.POST, params = {
		"tripId", "selectedSurvivalClasses", "save"
	})
	public ModelAndView manageSurvivalClasses(final int tripId, @RequestParam("selectedSurvivalClasses") final Collection<SurvivalClass> selectedSurvivalClasses) {
		ModelAndView result;
		Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		trip.setSurvivalClasses(new HashSet<SurvivalClass>(selectedSurvivalClasses));

		this.tripService.save(trip);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Removing SurvivalClass ----------------------------------------------------------------
	@RequestMapping(value = "/removeSurvivalClass", method = RequestMethod.GET)
	public ModelAndView removeSurvivalClass(@RequestParam("tripId") final int tripId, @RequestParam("survivalClassId") final int survivalClassId) {
		ModelAndView result;
		Trip trip;
		SurvivalClass survivalClass;

		trip = this.tripService.findOne(tripId);
		survivalClass = this.survivalClassService.findOne(survivalClassId);
		Assert.notNull(trip);
		trip.getSurvivalClasses().remove(survivalClass);
		this.tripService.save(trip);
		result = new ModelAndView("redirect:/trip/detailed-trip.do?tripId=" + tripId + "&anonymous=false");

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
		final Collection<SurvivalClass> notAddedSurvivalClasses;

		rangers = this.rangerService.findAll();
		legalTexts = this.legalTextService.findAllFinalMode();
		tags = this.tagService.findAll();
		categories = this.categoryService.findAll();
		notAddedSurvivalClasses = this.survivalClassService.findAll();
		notAddedSurvivalClasses.removeAll(trip.getSurvivalClasses());

		result = new ModelAndView("trip/edit");
		result.addObject("rangers", rangers);
		result.addObject("legalTexts", legalTexts);
		result.addObject("tags", tags);
		result.addObject("categories", categories);
		result.addObject("trip", trip);
		result.addObject("survivalClasses", notAddedSurvivalClasses);

		result.addObject("message", messageCode);

		return result;

	}
}
