
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.Hours;
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
import services.ManagerService;
import services.SearchService;
import services.SurvivalClassService;
import services.TripService;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.Explorer;
import domain.Manager;
import domain.Search;
import domain.SurvivalClass;
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
	@Autowired
	ManagerService			managerService;
	@Autowired
	SearchService			searchService;


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

	// Paging list -------------------------------------------------------------
	//	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "page")
	//	public ModelAndView list(@RequestParam final int page) throws IllegalArgumentException, IllegalAccessException, IOException {
	//		ModelAndView result;
	//		Collection<Trip> trips;
	//		Page<Trip> tripsPage;
	//		Pageable pageable;
	//		final Configuration configuration;
	//
	//		result = new ModelAndView("trip/list");
	//		configuration = this.configurationService.findConfiguration();
	//		pageable = new PageRequest(page, configuration.getMaxResults());
	//
	//		tripsPage = this.tripService.findPublicatedTrips(pageable);
	//		trips = tripsPage.getContent();
	//
	//		result.addObject("trips", trips);
	//		result.addObject("pageNum", tripsPage.getTotalPages());
	//		result.addObject("requestUri", "trip/list.do");
	//
	//		return result;
	//	}

	// listing -------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
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
		result.addObject("requestUri", "trip/search.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "categoryId")
	public ModelAndView listCategory(@RequestParam final int categoryId, @RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		final Page<Trip> tripsPage;
		Category category;
		Pageable pageable;
		Configuration configuration;

		result = new ModelAndView("trip/list");
		category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.tripService.findTrips(category, pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do?categoryId=" + categoryId);

		return result;
	}
	@RequestMapping(value = "/listExplorer", method = RequestMethod.GET)
	public ModelAndView listExplorer(@RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		Configuration configuration;
		Explorer explorer;
		Search search;

		result = new ModelAndView("trip/list");
		explorer = (Explorer) this.actorService.findActorByPrincipal();
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		search = explorer.getSearch();
		final int hours = Hours.hoursBetween(new DateTime(search.getSearchMoment()), new DateTime(new Date())).getHours();
		if (hours <= 1)
			tripsPage = this.tripService.findTripsBySearchParameters(search, pageable);
		else {
			tripsPage = this.tripService.findPublicatedTrips(pageable);
			search.setKeyWord("");
			search.setTrips(new HashSet<Trip>());
			search.setDateRangeStart(null);
			search.setDateRangeEnd(null);
			search.setPriceRangeStart(null);
			search.setPriceRangeEnd(null);
			this.searchService.save(search);
		}
		trips = tripsPage.getContent();
		result.addObject("trips", trips);
		result.addObject("search", search);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/listExplorer.do");

		return result;
	}

	// Searching --------------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam(value = "keyword", defaultValue = "") final String keyword, @RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;

		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.tripService.findTripsBySearchParameters(keyword, pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("keyword", keyword);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/search.do");

		return result;
	}

	@RequestMapping(value = "/search-ajax", method = RequestMethod.POST)
	public ModelAndView searchAjax(@RequestParam(value = "keyword", defaultValue = "") final String keyword, @RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;

		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list-ajax");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.tripService.findTripsBySearchParameters(keyword, pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("keyword", keyword);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/search.do");

		return result;
	}

	@RequestMapping(value = "/searchExplorer", method = RequestMethod.POST, params = "save")
	public ModelAndView searchExplorer(@Valid final Search search, final BindingResult binding, @RequestParam(value = "page", required = false, defaultValue = "0") final int page) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;

		Pageable pageable;
		Configuration configuration;

		if (binding.hasErrors()) {
			result = new ModelAndView("trip/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getMaxResults());

			tripsPage = this.tripService.findPublicatedTrips(pageable);
			trips = tripsPage.getContent();

			result.addObject("trips", trips);
			result.addObject("search", search);
			result.addObject("pageNum", tripsPage.getTotalPages());
			result.addObject("requestUri", "trip/listExplorer.do");
		} else
			try {
				final Search s;

				result = new ModelAndView("trip/list");
				configuration = this.configurationService.findConfiguration();
				pageable = new PageRequest(0, configuration.getMaxResults());

				tripsPage = this.tripService.findTripsBySearchParameters(search, pageable);
				trips = tripsPage.getContent();

				search.setTrips(trips);
				s = this.searchService.save(search);

				result.addObject("trips", trips);
				result.addObject("search", s);
				result.addObject("pageNum", tripsPage.getTotalPages());
				result.addObject("requestUri", "trip/listExplorer.do");
			} catch (final Throwable oops) {

				//TODO: IMPORTANTE
				oops.printStackTrace();
				result = new ModelAndView("trip/list");
				configuration = this.configurationService.findConfiguration();
				pageable = new PageRequest(0, configuration.getMaxResults());

				tripsPage = this.tripService.findPublicatedTrips(pageable);
				trips = tripsPage.getContent();

				result.addObject("trips", trips);
				result.addObject("search", search);
				result.addObject("pageNum", tripsPage.getTotalPages());
				result.addObject("requestUri", "trip/listExplorer.do");
			}

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
		final List<SurvivalClass> survivalClasses;
		final List<Boolean> survivalClassesJoinedIndexed = new ArrayList<Boolean>();
		final Explorer explorer;
		try {
			result = new ModelAndView("trip/detailed-trip");
			random = new Random();
			trip = this.tripService.findOne(tripId);
			survivalClasses = new ArrayList<SurvivalClass>(trip.getSurvivalClasses());
			hasManager = false;
			hasExplorer = false;
			//			Assert.isTrue(trip.getPublicationDate().before(new Date()));

			if (anonymous == false) {
				actor = this.actorService.findActorByPrincipal();

				if (actor instanceof Manager && trip.getManagers().contains(actor))
					hasManager = true;
				else if (actor instanceof Explorer && this.tripService.getAcceptedTripsFromExplorerId(actor.getId()).contains(trip)) {
					hasExplorer = true;
					Assert.isTrue(trip.getPublicationDate().before(new Date()));

					explorer = (Explorer) actor;
					// Añade una lista paralela si el explorer esta inscrito o no en esa survivalClass
					for (final SurvivalClass sv : survivalClasses)
						survivalClassesJoinedIndexed.add(explorer.getSurvivalClasses().contains(sv));

					result.addObject("survivalClassesJoinedIndexed", survivalClassesJoinedIndexed);
				} else
					Assert.isTrue(trip.getPublicationDate().before(new Date()));

			} else
				Assert.isTrue(trip.getPublicationDate().before(new Date()));
			result.addObject("survivalClasses", survivalClasses);
			result.addObject("trip", trip);
			if (trip.getSponsorships().size() > 0)
				result.addObject("sponsorship", trip.getSponsorships().toArray()[random.nextInt(trip.getSponsorships().size())]);
			else
				result.addObject("sponsorship", null);

			result.addObject("hasManager", hasManager);
			result.addObject("hasExplorer", hasExplorer);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/index.do");
		}
		return result;

	}

}
