
package controllers.auditor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditorService;
import services.NoteService;
import services.TripService;
import controllers.AbstractController;
import domain.Note;

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
		note = this.noteService.create();
		note.setTrip(this.tripService.findOne(tripId));
		result = this.createEditModelAndView(note);

		return result;
	}
	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int noteId) {
		ModelAndView result;
		Note note;

		note = this.noteService.findOne(noteId);

		result = this.createEditModelAndView(note);

		return result;
	}
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
