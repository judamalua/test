
package controllers.manager;

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

import security.LoginService;
import services.ActorService;
import services.ManagerService;
import services.NoteService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
import domain.Note;

@Controller
@RequestMapping("note/manager")
public class NoteManagerController extends AbstractController {

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
		Manager replierManager;

		try {
			note = this.noteService.findOne(noteId);

			replierManager = (Manager) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
			Assert.isTrue(note.getReply() == null || note.getReply().equals(""));
			result = this.createEditModelAndView(note);
			result.addObject("manager", replierManager);
			result.addObject("momentOfReply", new Date(System.currentTimeMillis() - 1));
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
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
				result = new ModelAndView("redirect:/trip/manager/list.do");
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
