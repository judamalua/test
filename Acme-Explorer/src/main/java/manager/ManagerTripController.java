
package manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ManagerService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
import domain.Trip;

@Controller
@RequestMapping("/trip/manager")
public class ManagerTripController extends AbstractController {

	@Autowired
	TripService		tripService;
	@Autowired
	ActorService	actorService;
	@Autowired
	ManagerService	managerService;


	// Constructors -----------------------------------------------------------

	public ManagerTripController() {
		super();
	}

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

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tripId) {
		ModelAndView result;
		Trip trip;

		result = new ModelAndView("trip/edit");

		trip = this.tripService.findOne(tripId);

		result.addObject("trip", trip);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Trip trip;

		result = new ModelAndView("trip/edit");

		trip = this.tripService.create();

		result.addObject("trip", trip);

		return result;
	}
}
