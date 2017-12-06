
package controllers;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.SurvivalClassService;
import services.TripService;
import domain.Actor;
import domain.Configuration;
import domain.Trip;

@Controller
@RequestMapping("/trip")
public class TripController extends AbstractController {

	@Autowired
	TripService				tripService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	SurvivalClassService	survivalClassService;

	String[]				searchKeywordParams	= new String[] {
		"keyword", "page"
												};


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

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

	@RequestMapping(value = "/search", method = RequestMethod.GET, params = {
		"keyword", "search"
	})
	public ModelAndView search(@RequestParam("keyword") final String keyword, @RequestParam("search") final String search) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getMaxResults());

		tripsPage = this.tripService.findTrips(keyword, pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

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

			if (trip.getManagers().contains(actor))
				hasManager = true;

			if (this.tripService.getAcceptedTripsFromExplorerId(actor.getId()).contains(trip))
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
