
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageFolderService;
import domain.Actor;
import domain.MessageFolder;

@Controller
@RequestMapping("/MessageFolder")
public class MessageFolderController extends AbstractController {

	@Autowired
	MessageFolderService	messageFolderService;
	@Autowired
	ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public MessageFolderController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<MessageFolder> messageFolders;
		Actor actor;

		result = new ModelAndView("messageFolder/list");

		actor = this.actorService.findActorByPrincipal();

		messageFolders = actor.getMessageFolders();
		result.addObject("messageFolders", messageFolders);

		return result;
	}
	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list(@RequestParam final int messageFolderId) {
	//		ModelAndView result;
	//		MessageFolder messageFolder;
	//		Collection<MessageFolder> messageFolders;
	//
	//		result = new ModelAndView("messageFolder/list");
	//
	//		messageFolder = this.messageFolderService.findOne(messageFolderId);
	//
	//		messageFolders = messageFolder.getMessageFolderChildren();
	//		result.addObject("messageFolders", messageFolders);
	//
	//		return result;
	//	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;

		result = new ModelAndView("messageFolder/edit");

		messageFolder = this.messageFolderService.findOne(messageFolderId);

		result.addObject("messageFolder", messageFolder);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MessageFolder messageFolder;

		result = new ModelAndView("messageFolder/edit");

		messageFolder = this.messageFolderService.create();

		result.addObject("messageFolder", messageFolder);

		return result;
	}
}
