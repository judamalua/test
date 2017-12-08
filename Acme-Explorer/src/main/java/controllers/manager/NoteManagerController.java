
package controllers.manager;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ManagerService;
import services.NoteService;
import services.TripService;
import domain.Manager;
import domain.Note;

@Controller
@RequestMapping("note/manager")
public class NoteManagerController {

	@Autowired
	ManagerService	managerService;
	@Autowired
	ActorService	actorService;
	@Autowired
	NoteService		noteService;
	@Autowired
	TripService		tripService;


	// Constructors -----------------------------------------------------------

	public NoteManagerController() {
		super();
	}

	// Editing ---------------------------------------------------------------	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int noteId) {
		ModelAndView result;
		Note note;

		note = this.noteService.findOne(noteId);

		final Manager replierManager = (Manager) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		note.setMomentOfReply(new Date(System.currentTimeMillis() - 1000));
		note.setReplierManager(replierManager);

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
				result = new ModelAndView("redirect:trip/manager/list.do");
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
