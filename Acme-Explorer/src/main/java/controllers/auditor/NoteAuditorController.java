
package controllers.auditor;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditorService;
import services.ManagerService;
import services.NoteService;
import services.TripService;
import controllers.AbstractController;
import domain.Note;
import domain.Trip;

@Controller
@RequestMapping("note/auditor")
public class NoteAuditorController extends AbstractController {

	@Autowired
	AuditorService	auditorService;
	@Autowired
	ActorService	actorService;
	@Autowired
	NoteService		noteService;
	@Autowired
	ManagerService	managerService;
	@Autowired
	TripService		tripService;


	// Constructors -----------------------------------------------------------

	public NoteAuditorController() {
		super();
	}

	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("note/list");

		final Collection<Note> notes = this.noteService.findNotesByAuditorID();
		result.addObject("notes", notes);
		result.addObject("requestUri", "note/auditor/list.do");

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Note note;
		Trip trip;
		try {
			trip = this.tripService.findOne(tripId);
			Assert.isTrue(trip.getPublicationDate().before(new Date()));
			note = this.noteService.create();
			note.setTrip(trip);
			result = this.createEditModelAndView(note);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	//	// Editing ---------------------------------------------------------------	
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int noteId) {
	//
	//		ModelAndView result;
	//		Note note;
	//		Auditor actor;
	//		try {
	//			actor = (Auditor) this.actorService.findActorByPrincipal();
	//			note = this.noteService.findOne(noteId);
	//			Assert.isTrue(actor.getNotes().contains(note));
	//			result = this.createEditModelAndView(note);
	//		} catch (final Throwable oops) {
	//			result = new ModelAndView("redirect:/misc/403");
	//		}
	//		return result;
	//	}
	// Saving --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Note note, final BindingResult binding) {

		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(note);
		else
			try {
				this.noteService.save(note);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(note, "note.commit.error");

			}

		return result;
	}
	// Ancillary methods ---------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Note note) {
		ModelAndView result;

		result = this.createEditModelAndView(note, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Note note, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("note/edit");

		result.addObject("note", note);
		result.addObject("message", messageCode);

		return result;
	}
}
