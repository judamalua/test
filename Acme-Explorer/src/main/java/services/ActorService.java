
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.Explorer;
import domain.Message;
import domain.MessageFolder;
import domain.Ranger;
import domain.Trip;

@Service
@Transactional
public class ActorService {

	// Managed repository ---------------------------------------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;
	// Supporting services --------------------------------------------------------------------------
	@Autowired
	private UserAccountService		userAccountService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private TripService				tripService;
	@Autowired
	private ConfigurationService	configurationService;


	// Simple CRUD methods -------------------------------------------------------------------------

	public Collection<Actor> findAll() {

		Collection<Actor> result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.actorRepository.findAll();

		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {

		Assert.isTrue(actorId != 0);

		UserAccount userAccount;
		Actor actor;
		Actor result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor save(final Actor actor) {

		Assert.notNull(actor);

		UserAccount userAccount;
		Actor loggedActor;
		Actor result;
		String phoneNumberPrefix;

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		loggedActor = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(loggedActor);
		//Assert.isTrue(loggedActor.equals(actor)); TODO
		Assert.isTrue(actor.getIsBanned() == false || this.actorRepository.findOne(actor.getId()).getIsBanned());

		// Comprobación de las palabras de spam
		// TODO

		//Este bloque if realiza el requisito 35.2
		if (actor.getVersion() != 0)
			if (actor.getIsBanned() != this.actorRepository.findOne(actor.getId()).getIsBanned())
				if (actor.getIsBanned() == true)
					Assert.isTrue(actor.isSuspicious());

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if (!actor.getPhoneNumber().trim().startsWith("+") && !actor.getPhoneNumber().equals("")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = actor.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			actor.setPhoneNumber(finalPhoneNumber);

		}

		result = this.actorRepository.save(actor);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));
		this.checkUserLogin();

		this.actorRepository.delete(actor);

		for (final MessageFolder messageFolder : actor.getMessageFolders())
			this.messageFolderService.delete(messageFolder);
	}

	// Other business methods --------------------------------------------------------------------------

	//Functional Requeriment 10.1
	public Ranger registerRanger(final Ranger r) {

		Ranger result;
		UserAccount savedUserAccount;
		String password;
		Md5PasswordEncoder encoder;
		String phoneNumberPrefix;

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();

		Assert.notNull(r.getUserAccount());
		Assert.isTrue(!this.userAccountService.findAll().contains(r.getUserAccount()));
		Assert.isTrue(!this.actorRepository.exists((r.getId())));

		encoder = new Md5PasswordEncoder();

		password = r.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);
		r.getUserAccount().setPassword(password);

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if (!r.getPhoneNumber().trim().startsWith("+") && !r.getPhoneNumber().equals("")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = r.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			r.setPhoneNumber(finalPhoneNumber);

		}

		savedUserAccount = this.userAccountService.save(r.getUserAccount());

		r.setUserAccount(savedUserAccount);
		result = this.actorRepository.save(r);

		return result;
	}

	//Functional Requeriment 10.1
	public Explorer registerExplorer(final Explorer e) {

		Explorer result;
		Assert.isTrue(!this.userAccountService.findAll().contains(e.getUserAccount()));
		Assert.isTrue(!this.actorRepository.exists((e.getId())));

		final UserAccount savedUserAccount;
		String password;
		Md5PasswordEncoder encoder;
		String phoneNumberPrefix;

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();

		encoder = new Md5PasswordEncoder();

		password = e.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);
		e.getUserAccount().setPassword(password);

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if (!e.getPhoneNumber().trim().startsWith("+") && !e.getPhoneNumber().equals("")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = e.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			e.setPhoneNumber(finalPhoneNumber);

		}

		savedUserAccount = this.userAccountService.save(e.getUserAccount());

		e.setUserAccount(savedUserAccount);

		result = this.actorRepository.save(e);

		return result;
	}

	public Actor registerActor(final Actor actor) {

		Actor result;
		UserAccount savedUserAccount;
		String password;
		Md5PasswordEncoder encoder;
		String phoneNumberPrefix;

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();

		Assert.notNull(actor.getUserAccount());
		Assert.isTrue(!this.userAccountService.findAll().contains(actor.getUserAccount()));
		Assert.isTrue(!this.actorRepository.exists((actor.getId())));

		encoder = new Md5PasswordEncoder();

		password = actor.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);
		actor.getUserAccount().setPassword(password);

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if (!actor.getPhoneNumber().trim().startsWith("+") && !actor.getPhoneNumber().equals("")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = actor.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			actor.setPhoneNumber(finalPhoneNumber);

		}

		savedUserAccount = this.userAccountService.save(actor.getUserAccount());

		actor.setUserAccount(savedUserAccount);
		result = this.actorRepository.save(actor);

		return result;
	}

	//Functional requeriment 10.2
	public Collection<Trip> findAllTrips() {

		Collection<Trip> result;

		result = this.tripService.findAll();

		return result;
	}

	public Collection<Trip> searchTrips(final String keyword) {

		Collection<Trip> result;

		result = this.tripService.findTrips(keyword);

		return result;
	}

	public Collection<Trip> searchTrips(final Category category) {

		Collection<Trip> result;

		result = this.tripService.findTrips(category);

		return result;
	}

	public Actor findActorByUserAccountId(final int id) {

		Assert.notNull(id);
		Assert.isTrue(id != 0);

		final Actor result;

		result = this.actorRepository.findActorByUserAccountId(id);
		//Assert.notNull(result);

		return result;
	}

	public void sendMessage(final Message message, final Actor sender, final Actor receiver, MessageFolder messageFolderReceiver) {

		Assert.notNull(message);
		this.checkUserLogin();

		UserAccount userAccount;
		Message savedMessage, savedMessageCopy, messageCopy;
		MessageFolder messageFolderSender, outBoxSender;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		Assert.notNull(sender);
		Boolean badActor = false;

		for (final String s : this.configurationService.findConfiguration().getSpamWords())
			if (message.getBody().toLowerCase().contains(s.toLowerCase()))
				badActor = true;
		if (badActor == true) {
			message.getSender().setSuspicious(true);
			this.actorRepository.save(message.getSender());
		}
		messageCopy = this.messageService.copyMessage(message);

		if (messageCopy.getBody() != null && this.checkSpamWords(messageCopy))
			messageFolderReceiver = this.messageFolderService.findMessageFolder("spam box", receiver);

		messageCopy.setMessageFolder(messageFolderReceiver);

		outBoxSender = this.messageFolderService.findMessageFolder("out box", sender);
		if (!outBoxSender.getMessages().contains(message)) {
			savedMessage = this.messageService.save(message);
			messageFolderSender = savedMessage.getMessageFolder();
			messageFolderSender.getMessages().add(savedMessage);
			this.messageFolderService.save(messageFolderSender);
		}
		savedMessageCopy = this.messageService.save(messageCopy);

		messageFolderReceiver.getMessages().add(savedMessageCopy);

		this.messageFolderService.save(messageFolderReceiver);

	}

	public void sendMessage(final Message message, final Actor sender, final Actor receiver) {

		Assert.notNull(message);
		this.checkUserLogin();

		UserAccount userAccount;
		Message savedMessage, savedMessageCopy, messageCopy;
		MessageFolder messageFolderSender, messageFolderReceiver;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		Assert.notNull(sender);
		Boolean badActor = false;

		for (final String s : this.configurationService.findConfiguration().getSpamWords())
			if (message.getBody().toLowerCase().contains(s.toLowerCase()))
				badActor = true;
		if (badActor == true) {
			message.getSender().setSuspicious(true);
			this.actorRepository.save(message.getSender());
		}
		messageCopy = this.messageService.copyMessage(message);

		if (messageCopy.getBody() != null && !this.checkSpamWords(messageCopy))
			messageFolderReceiver = this.messageFolderService.findMessageFolder("in box", receiver);
		else
			messageFolderReceiver = this.messageFolderService.findMessageFolder("spam box", receiver);
		messageCopy.setMessageFolder(messageFolderReceiver);

		savedMessage = this.messageService.save(message);
		savedMessageCopy = this.messageService.save(messageCopy);

		messageFolderSender = savedMessage.getMessageFolder();

		messageFolderReceiver.getMessages().add(savedMessageCopy);
		messageFolderSender.getMessages().add(savedMessage);

		this.messageFolderService.save(messageFolderSender);
		this.messageFolderService.save(messageFolderReceiver);

	}

	public Message moveMessage(final Message message, MessageFolder folder) {

		Assert.notNull(folder);
		Assert.isTrue(folder.getId() != 0);
		this.checkUserLogin();

		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));
		Assert.isTrue(actor.getMessageFolders().contains(folder));

		final MessageFolder messageFolderOr;
		Message result;

		messageFolderOr = message.getMessageFolder();
		messageFolderOr.getMessages().remove(message);

		this.messageFolderService.save(messageFolderOr);

		folder.getMessages().add(message);
		folder = this.messageFolderService.save(folder);

		message.setMessageFolder(folder);
		result = this.messageService.save(message);

		return result;
	}

	public void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

	public void checkMessageFolders(final Actor a) {

		Integer count;
		Collection<MessageFolder> messageFolders;

		count = 0;
		messageFolders = a.getMessageFolders();

		for (final MessageFolder mf : messageFolders)
			if (mf.getIsDefault() == true)
				count++;
		Assert.isTrue(count == 5);

	}

	private boolean checkSpamWords(final Message message) {

		Configuration configuration;
		boolean result;

		result = false;
		configuration = this.configurationService.findConfiguration();
		for (final String spam : configuration.getSpamWords()) {
			result = message.getBody().toLowerCase().contains(spam.toLowerCase());
			result = message.getSubject().toLowerCase().contains(spam.toLowerCase());
			if (result == true)
				break;

		}

		return result;
	}

	public Actor findActorByMessageFolder(final int id) {
		this.checkUserLogin();

		Actor result;

		result = this.actorRepository.findActorByMessageFolder(id);

		return result;
	}

	public Actor findActorByPrincipal() {

		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findActorByUserAccountId(userAccount.getId());

		Assert.notNull(result);

		return result;
	}

	public Collection<Actor> findSuspicious() {
		final Collection<Actor> result = this.actorRepository.findSuspicious();
		return result;
	}

	/**
	 * This method check if a String collection contains any spam word
	 * 
	 * @param strings
	 *            to check if any of its contained strings contains a spam word
	 * 
	 * @author Dani & Juanmi & Ale
	 */
	public void checkSpamWords(final Collection<String> stringsToCheck) {

		if (this.configurationService.findConfiguration() != null) {
			final Actor actor = this.actorRepository.findActorByUserAccountId(LoginService.getPrincipal().getId());
			final Collection<String> spamWords = this.configurationService.findConfiguration().getSpamWords();
			bucleStrings: for (final String s : stringsToCheck)
				for (final String spamWord : spamWords)
					if (s.toLowerCase().contains(spamWord.toLowerCase())) {
						actor.setSuspicious(true);
						this.save(actor);
						break bucleStrings;
					}
		}
	}

	/**
	 * This method check if a String contains any spam word
	 * 
	 * @param string
	 *            to check if contains a spam word
	 * 
	 * @author Dani & Juanmi & Ale
	 */
	public void checkSpamWords(final String stringSpam) {
		this.checkSpamWords(new HashSet<String>(Arrays.asList(stringSpam)));
	}
}
