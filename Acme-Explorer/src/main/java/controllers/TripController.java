
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.TripService;
import domain.Trip;

@Controller
@RequestMapping("/trip")
public class TripController extends AbstractController {

	@Autowired
	TripService		tripService;
	@Autowired
	ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Trip> trips;

		result = new ModelAndView("messageFolder/list");

		trips = this.tripService.findAll();
		result.addObject("trips", trips);

		return result;
	}

}
