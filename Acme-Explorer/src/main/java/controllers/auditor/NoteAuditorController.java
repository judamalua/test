
package controllers.auditor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditorService;
import services.NoteService;
import controllers.AbstractController;
import domain.Auditor;
import domain.Manager;
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
	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int noteId) {
		ModelAndView result;
		Note note;

		note = this.noteService.findOne(noteId);

		result = this.createEditModelAndView(note);

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
		Manager manager;
		Auditor auditor;
		Trip trip;

		manager = note.getReplierManager();
		auditor = note.getAuditor();
		trip = note.getTrip();

		result = new ModelAndView("note/edit");

		result.addObject("note", note);
		result.addObject("manager", manager);
		result.addObject("auditor", auditor);
		result.addObject("trip", trip);

		result.addObject("message", messageCode);

		return result;
	}
}
