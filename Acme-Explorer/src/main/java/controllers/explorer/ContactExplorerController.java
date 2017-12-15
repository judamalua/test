
package controllers.explorer;

import java.util.Collection;

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
import services.ConfigurationService;
import services.ContactService;
import controllers.AbstractController;
import domain.Contact;
import domain.Explorer;

@Controller
@RequestMapping("/contact/explorer")
public class ContactExplorerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ContactService			contactService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;


	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		Explorer explorer;
		final Collection<Contact> contacts;

		result = new ModelAndView("contact/list");

		explorer = (Explorer) this.actorService.findActorByPrincipal();

		contacts = explorer.getContacts();
		result.addObject("contacts", contacts);

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int contactId) {
		ModelAndView result;
		Contact contact;

		try {
			contact = this.contactService.findOne(contactId);
			Assert.notNull(contact);
			result = this.createEditModelAndView(contact);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Contact contact;

		contact = this.contactService.create();
		result = this.createEditModelAndView(contact);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Contact contact, final BindingResult binding) {
		ModelAndView result;
		final Boolean error = true;

		if (binding.hasErrors())
			result = this.createEditModelAndView(contact, "contact.params.error");
		else
			try {
				this.contactService.save(contact);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(contact, "contact.commit.error");
				if ((contact.getEmail() == "" || contact.getEmail() == null) && (contact.getPhoneNumber() == "" || contact.getPhoneNumber() == null))
					result.addObject("error", error);
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Contact contact, final BindingResult binding) {
		ModelAndView result;

		try {
			this.contactService.delete(contact);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(contact, "contact.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Contact contact) {
		ModelAndView result;

		result = this.createEditModelAndView(contact, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Contact contact, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("contact/edit");
		result.addObject("contact", contact);

		result.addObject("message", messageCode);

		return result;

	}
}
