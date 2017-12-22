
package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.ConfigurationService;
import services.LegalTextService;
import services.ManagerService;
import services.RangerService;
import services.StageService;
import services.SurvivalClassService;
import services.TagService;
import services.TripService;
import controllers.AbstractController;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.LegalText;
import domain.Manager;
import domain.Ranger;
import domain.Stage;
import domain.SurvivalClass;
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

	@Autowired
	ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public TripManagerController() {
		super();
	}

	// Listing ------------------------------------------------------------------

	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list() {
	//		ModelAndView result;
	//		Collection<Trip> trips;
	//		final Manager manager;
	//
	//		result = new ModelAndView("trip/list");
	//		manager = (Manager) this.actorService.findActorByPrincipal();
	//
	//		trips = this.managerService.findTripsByManager(manager.getId());
	//		result.addObject("trips", trips);
	//		result.addObject("requestUri", "trip/manager/list.do");
	//
	//		return result;
	//	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listCategory(@RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		final Page<Trip> tripsPage;
		Pageable pageable;
		Configuration configuration;
		Actor actor;

		result = new ModelAndView("trip/list");

		actor = this.actorService.findActorByPrincipal();
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.managerService.findTripsByManager(actor.getId(), pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/manager/list.do");

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "tripId", defaultValue = "-1") final int tripId) {
		ModelAndView result;
		Trip trip;
		Manager manager;
		try {
			Assert.isTrue(tripId != -1);
			trip = this.tripService.findOne(tripId);
			Assert.notNull(trip);
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			manager = (Manager) this.actorService.findActorByPrincipal();
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			Assert.isTrue(this.managerService.findTripsByManager(manager.getId()).contains(trip));
			result = this.createEditModelAndView(trip);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

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
		result = new ModelAndView("redirect:/trip/list.do");

		return result;
	}

	// Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam("tripId") final int tripId) {
		ModelAndView result;
		Trip trip;
		try {
			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getCancelReason() == null || trip.getCancelReason().equals(""));
			Assert.isTrue(trip.getPublicationDate().before(new Date()) && trip.getStartDate().after(new Date()));
			Assert.notNull(trip);
			result = new ModelAndView("trip/cancel-trip");
			result.addObject("trip", tripId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, params = {
		"save", "reason", "tripId"
	})
	public ModelAndView SaveCancel(final int tripId, final String reason) {
		ModelAndView result;
		Trip trip;
		try {
			trip = this.tripService.findOne(tripId);
			trip.setCancelReason(reason);
			this.tripService.save(trip);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving -----------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save", "titleStage", "descriptionStage", "priceStage"
	})
	public ModelAndView create(@RequestParam("titleStage") final String titleStage, @RequestParam("descriptionStage") final String descriptionStage, @RequestParam("priceStage") final double priceStage, @Valid final Trip trip, final BindingResult binding) {
		ModelAndView result;
		Stage stage;
		stage = this.stageService.create();

		if (binding.hasErrors())
			result = this.createEditModelAndView(trip, "trip.params.error");
		else
			try {
				stage.setTitle(titleStage);
				stage.setPrice(priceStage);
				stage.setDescription(descriptionStage);
				trip.getStages().add(stage);
				//trip.getTags().remove(null);
				//				if (trip.getTags().equals(null))
				//					trip.setTags(new HashSet<Tag>());
				this.tripService.save(trip);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				trip.getStages().remove(stage);
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
				//				trip.getTags().remove(null);
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
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
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
		final Collection<SurvivalClass> survivalClasses;
		final List<Boolean> indexedSurvivalClasses;
		final Manager manager;

		try {

			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			Assert.notNull(trip);
			result = new ModelAndView("trip/manageSurvivalClasses");
			manager = (Manager) this.actorService.findActorByPrincipal();

			survivalClasses = new HashSet<SurvivalClass>();
			indexedSurvivalClasses = new ArrayList<Boolean>();

			//			for (final SurvivalClass sv : trip.getSurvivalClasses())
			//				if (sv.getOrganisationMoment().after(new Date()))
			//					survivalClasses.add(sv);

			for (final SurvivalClass sv : manager.getSurvivalClasses())
				if (sv.getOrganisationMoment().after(new Date()))
					survivalClasses.add(sv);

			for (final SurvivalClass sv : survivalClasses)
				indexedSurvivalClasses.add(trip.getSurvivalClasses().contains(sv));
			result.addObject("trip", tripId);
			result.addObject("survivalClasses", survivalClasses);
			result.addObject("indexedSurvivalClasses", indexedSurvivalClasses);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving Canceling ----------------------------------------------------------------
	@RequestMapping(value = "/manageSurvivalClasses", method = RequestMethod.POST, params = {
		"tripId", "save"
	})
	public ModelAndView manageSurvivalClasses(final int tripId, @RequestParam(value = "selectedSurvivalClasses", required = false) Collection<SurvivalClass> selectedSurvivalClasses) {
		ModelAndView result;
		Trip trip;
		try {
			if (selectedSurvivalClasses == null)
				selectedSurvivalClasses = new HashSet<SurvivalClass>();
			trip = this.tripService.findOne(tripId);
			Assert.notNull(trip);
			trip.setSurvivalClasses(new HashSet<SurvivalClass>(selectedSurvivalClasses));
			this.tripService.save(trip);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	// Removing SurvivalClass ----------------------------------------------------------------
	@RequestMapping(value = "/removeSurvivalClass", method = RequestMethod.GET)
	public ModelAndView removeSurvivalClass(@RequestParam("tripId") final int tripId, @RequestParam("survivalClassId") final int survivalClassId) {
		ModelAndView result;
		Trip trip;
		SurvivalClass survivalClass;
		try {
			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getPublicationDate().after(new Date()));
			survivalClass = this.survivalClassService.findOne(survivalClassId);
			Assert.notNull(trip);
			trip.getSurvivalClasses().remove(survivalClass);
			this.tripService.save(trip);
			result = new ModelAndView("redirect:/trip/detailed-trip.do?tripId=" + tripId + "&anonymous=false");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
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
		//		final Collection<Tag> tags;
		final Collection<Category> categories;
		final Collection<SurvivalClass> notAddedSurvivalClasses;
		//		final Collection<TagValue> tagValues;

		rangers = this.rangerService.findAll();
		legalTexts = this.legalTextService.findAllFinalMode();
		//		tags = this.tagService.findAll();
		categories = this.categoryService.findAll();
		notAddedSurvivalClasses = this.survivalClassService.findAll();
		notAddedSurvivalClasses.removeAll(trip.getSurvivalClasses());

		result = new ModelAndView("trip/edit");
		result.addObject("rangers", rangers);
		result.addObject("legalTexts", legalTexts);
		//		result.addObject("tagsTrip", tags);
		result.addObject("categories", categories);
		result.addObject("trip", trip);
		//		result.addObject("notAddedSurvivalClasses", notAddedSurvivalClasses);

		result.addObject("message", messageCode);

		return result;

	}
}
