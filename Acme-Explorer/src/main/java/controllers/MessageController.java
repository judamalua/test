
package controllers;

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
import services.MessageFolderService;
import services.MessageService;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	MessageService			messageService;
	@Autowired
	ActorService			actorService;
	@Autowired
	MessageFolderService	messageFolderService;


	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "messageFolderId")
	public ModelAndView list(@RequestParam final int messageFolderId) {
		final ModelAndView result;
		final MessageFolder messageFolder;
		Collection<Message> messages;

		messageFolder = this.messageFolderService.findOne(messageFolderId);
		messages = messageFolder.getMessages();

		result = new ModelAndView("message/list");
		result.addObject("messageFolder", messageFolder);
		result.addObject("messages", messages);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Message message, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(message, "message.params.error");
		else
			try {
				this.actorService.sendMessage(message, message.getSender(), message.getReceiver());
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message message;
		Actor actor;

		message = this.messageService.create();
		actor = this.actorService.findActorByPrincipal();
		message.setSender(actor);
		result = this.createEditModelAndView(message);

		return result;
	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		ModelAndView result;
		final Collection<Actor> actors;

		actors = this.actorService.findAll();

		result = new ModelAndView("trip/edit");
		result.addObject("row", message);
		result.addObject("actors", actors);
		result.addObject("message", messageCode);

		return result;

	}
}
