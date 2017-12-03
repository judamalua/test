
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MessageFolderService;
import services.MessageService;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	MessageService			messageService;

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
}
