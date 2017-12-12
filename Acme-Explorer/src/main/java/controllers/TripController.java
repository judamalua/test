
package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.ConfigurationService;
import services.SurvivalClassService;
import services.TripService;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.Explorer;
import domain.Manager;
import domain.Trip;

@Controller
@RequestMapping("/trip")
public class TripController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	TripService				tripService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	SurvivalClassService	survivalClassService;
	@Autowired
	CategoryService			categoryService;


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

	// Paging list -------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "page")
	public ModelAndView list(@RequestParam final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.tripService.findPublicatedTrips(pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	// listing -------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getMaxResults());

		tripsPage = this.tripService.findPublicatedTrips(pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "categoryId")
	public ModelAndView listCategory(@RequestParam final int categoryId) {
		ModelAndView result;
		Collection<Trip> trips;
		Category category;

		result = new ModelAndView("trip/list");
		category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);

		trips = this.tripService.findTrips(category);

		result.addObject("trips", trips);

		return result;
	}

	// Searching --------------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(@RequestParam(value = "keyword", defaultValue = "") final String keyword, @RequestParam(value = "startPrice", defaultValue = "0.0") final double startPrice,
		@RequestParam(value = "endPrice", defaultValue = "10000.0") final double endprice, @RequestParam(value = "", defaultValue = "2000/01/01 00:00") final Date startDate,
		@RequestParam(value = "endDate", defaultValue = "2999/01/01 00:00") final Date endDate) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getMaxResults());

		tripsPage = this.tripService.findTripsBySearchParameters(keyword, startPrice, endprice, startDate, endDate, pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	// Detailing -----------------------------------------------------------------
	@RequestMapping(value = "/detailed-trip", method = RequestMethod.GET, params = {
		"tripId", "anonymous"
	})
	public ModelAndView detailedList(@RequestParam("tripId") final int tripId, @RequestParam("anonymous") final boolean anonymous) {
		ModelAndView result;
		final Trip trip;
		final Random random;
		boolean hasManager;
		boolean hasExplorer;
		final Actor actor;

		result = new ModelAndView("trip/detailed-trip");
		random = new Random();
		trip = this.tripService.findOne(tripId);
		hasManager = false;
		hasExplorer = false;

		if (anonymous == false) {
			actor = this.actorService.findActorByPrincipal();

			if (actor instanceof Manager && trip.getManagers().contains(actor))
				hasManager = true;

			if (actor instanceof Explorer && this.tripService.getAcceptedTripsFromExplorerId(actor.getId()).contains(trip))
				hasExplorer = true;

		}

		result.addObject("trip", trip);
		if (trip.getSponsorships().size() > 0)
			result.addObject("sponsorship", trip.getSponsorships().toArray()[random.nextInt(trip.getSponsorships().size())]);
		else
			result.addObject("sponsorship", null);

		result.addObject("hasManager", hasManager);
		result.addObject("hasExplorer", hasExplorer);

		return result;
	}
}
