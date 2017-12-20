
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	// Services -------------------------------------------------------
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
	public ModelAndView save(@ModelAttribute("modelMessage") @Valid final Message message, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(message, "message.params.error");
		else
			try {
				this.actorService.sendMessage(message, message.getSender(), message.getReceiver());
				result = new ModelAndView("redirect:list.do?messageFolderId=" + message.getMessageFolder().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save", "broadcast"
	})
	public ModelAndView saveBroadcast(@RequestParam("broadcast") final boolean broadcast, @ModelAttribute("modelMessage") @Valid final Message message, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(message, "message.params.error");
		else
			try {
				if (broadcast == true)
					this.messageService.broadcastNotification(message);
				else
					this.actorService.sendMessage(message, message.getSender(), message.getReceiver());

				result = new ModelAndView("redirect:list.do?messageFolderId=" + message.getMessageFolder().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("messageId") final int messageId, @RequestParam("messageFolderId") final int messageFolderId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);
		this.messageService.delete(message);

		result = new ModelAndView("redirect:/message/list.do?messageFolderId=" + messageFolderId);

		return result;
	}

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId) {
		ModelAndView result;
		Actor actor;
		Collection<MessageFolder> messageFolders;

		actor = this.actorService.findActorByPrincipal();
		messageFolders = actor.getMessageFolders();

		result = new ModelAndView("message/move");
		result.addObject("messageFolders", messageFolders);
		result.addObject("messageId", messageId);

		return result;
	}

	@RequestMapping(value = "/move", method = RequestMethod.POST, params = {
		"selectedMessageFolder", "messageId", "save"
	})
	public ModelAndView SaveMove(@ModelAttribute("messageId") final int messageId, @ModelAttribute("selectedMessageFolder") @Valid final MessageFolder selectedMessageFolder, final BindingResult binding) {
		ModelAndView result;
		Message message;
		if (binding.hasErrors())
			result = new ModelAndView("redirect:move.do");
		else
			try {
				message = this.messageService.findOne(messageId);
				this.actorService.moveMessage(message, selectedMessageFolder);
				result = new ModelAndView("redirect:/message/list.do?messageFolderId=" + message.getMessageFolder().getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:move.do");
			}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message message;

		message = this.messageService.create();

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

		result = new ModelAndView("message/edit");
		result.addObject("modelMessage", message);
		result.addObject("actors", actors);
		result.addObject("message", messageCode);

		return result;

	}
}
