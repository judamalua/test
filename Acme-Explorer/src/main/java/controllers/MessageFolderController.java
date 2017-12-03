
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
import domain.Actor;
import domain.MessageFolder;

@Controller
@RequestMapping("/messageFolder")
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

		result = new ModelAndView("messageFolder/list");

		messageFolders = this.messageFolderService.findRootMessageFolders();
		result.addObject("messageFolders", messageFolders);
		result.addObject("father", null);

		return result;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "messageFolderId")
	public ModelAndView list(@RequestParam final int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;
		Collection<MessageFolder> messageFolders;

		result = new ModelAndView("messageFolder/list");

		messageFolder = this.messageFolderService.findOne(messageFolderId);

		messageFolders = messageFolder.getMessageFolderChildren();
		result.addObject("messageFolders", messageFolders);
		result.addObject("father", messageFolder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;
		Collection<MessageFolder> messageFolders;
		Actor actor;

		result = new ModelAndView("messageFolder/edit");
		actor = this.actorService.findActorByPrincipal();
		messageFolder = this.messageFolderService.findOne(messageFolderId);
		messageFolders = actor.getMessageFolders();
		actor.getMessageFolders().removeAll(messageFolder.getMessageFolderChildren());

		result.addObject("messageFolder", messageFolder);
		result.addObject("messageFolders", messageFolders);

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageFolder, "messageFolder.params.error");
		else
			try {
				this.messageFolderService.save(messageFolder);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;

		try {
			this.messageFolderService.delete(messageFolder);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();

		result = this.createEditModelAndView(messageFolder);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder) {
		ModelAndView result;

		result = this.createEditModelAndView(messageFolder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder, final String messageCode) {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();
		result = new ModelAndView("messageFolder/edit");

		result.addObject("messageFolder", messageFolder);
		result.addObject("messageFolders", actor.getMessageFolders());

		return result;
	}
}
