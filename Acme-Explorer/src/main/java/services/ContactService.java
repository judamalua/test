
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ContactRepository;
import security.LoginService;
import domain.Contact;
import domain.Explorer;

@Service
@Transactional
public class ContactService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ContactRepository		contactRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Simple CRUD methods --------------------------------------------------
	public Contact create() {
		Contact result;

		result = new Contact();

		return result;
	}

	public Collection<Contact> findAll() {

		Collection<Contact> result;

		Assert.notNull(this.contactRepository);
		result = this.contactRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Contact findOne(final int contactId) {

		Contact result;
		final Explorer e = (Explorer) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		result = this.contactRepository.findOne(contactId);

		if (contactId != 0)
			Assert.isTrue(e.getContacts().contains(result));

		return result;

	}

	public Contact save(final Contact contact) {

		assert contact != null;

		Contact result;
		String phoneNumberPrefix;

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(contact.getName());
		if (contact.getEmail() != null)
			this.actorService.checkSpamWords(contact.getEmail());

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();

		final Explorer e = (Explorer) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());

		if (contact.getId() != 0)
			Assert.isTrue(e.getContacts().contains(contact));

		Assert.isTrue(!((contact.getEmail() == "" || contact.getEmail() == null) && (contact.getPhoneNumber() == "" || contact.getPhoneNumber() == null)));

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if ((contact.getPhoneNumber() != null) && !contact.getPhoneNumber().equals("") && !contact.getPhoneNumber().trim().startsWith("+")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = contact.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			contact.setPhoneNumber(finalPhoneNumber);

		}

		e.getContacts().remove(contact);
		result = this.contactRepository.save(contact);
		e.getContacts().add(result);

		Assert.isTrue(e.getContacts().contains(result));

		return result;

	}
	public void delete(final Contact c) {

		assert c != null;
		assert c.getId() != 0;

		Assert.isTrue(this.contactRepository.exists(c.getId()));
		final Explorer e = (Explorer) this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
		if (e.getContacts().contains(c)) {
			this.contactRepository.delete(c);
			e.getContacts().remove(c);
			Assert.isTrue(!this.contactRepository.findAll().contains(c));
		}

	}

}
