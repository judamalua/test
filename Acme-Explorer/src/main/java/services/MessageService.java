
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Service
@Transactional
public class MessageService {

	// Managed repository ---------------------------------------------------------------------------
	@Autowired
	private MessageRepository		messageRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;


	// Simple CRUD methods --------------------------------------------------

	public Message create() {
		Message result;

		result = new Message();

		return result;
	}

	public Collection<Message> findAll() {

		Collection<Message> result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		//		final Authority authority = new Authority();
		//		authority.setAuthority(Authority.ADMIN);
		//
		//		Assert.isTrue(userAccount.getAuthorities().contains(authority)); TODO Revisar

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Message findOne(final int messageId) {

		Assert.isTrue(messageId != 0);

		UserAccount userAccount;
		Actor actor;
		Message result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(messageId != 0);
		Assert.isTrue(this.messageRepository.exists(messageId));

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);
		Assert.isTrue(actor.getMessageFolders().contains(result.getMessageFolder()));

		return result;
	}

	public Message save(final Message message) {

		Assert.notNull(message);

		UserAccount userAccount;
		Actor actor;
		Message result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		//		for (int i = 0; i < actor.getMessageFolders().size(); i++)
		//			if (actor.getMessageFolders().toArray()[i].equals(message.getMessageFolder())) {
		//				final MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[i];
		//				messageFolder.getMessages().add(message);
		//				this.messageFolderService.save(messageFolder);
		//				this.actorService.save(actor);
		//				break;
		//			}
		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		result = this.messageRepository.save(message);

		return result;
	}
	public void delete(final Message message) {

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		UserAccount userAccount;
		Actor actor;
		MessageFolder messageFolder;
		MessageFolder trashBox;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));

		messageFolder = message.getMessageFolder();
		if (messageFolder.getName() == "trash box" && messageFolder.getIsDefault() == true) {
			messageFolder.getMessages().remove(message);
			this.messageFolderService.save(messageFolder);
			this.messageRepository.delete(message);
		} else {
			trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
			this.actorService.moveMessage(message, trashBox);
		}

	}
	// Other business methods --------------------------------------------------

	public Message copyMessage(final Message message) {

		Message result;

		result = this.create();
		result.setBody(message.getBody());
		result.setMessageFolder(message.getMessageFolder());
		result.setPriority(message.getPriority());
		result.setReceiver(message.getReceiver());
		result.setSender(message.getSender());
		result.setSubject(message.getSubject());

		return result;
	}

	// Requirement 14.5: An administrator must be able to send a broadcast notification.
	public void broadcastNotification(final Message message) {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final String box = message.getMessageFolder().getName();
		Assert.isTrue(box.equals("notification box"));
		final Collection<Actor> allActors = this.actorService.findAll();

		for (final Actor a : allActors) {
			Assert.notNull(a);
			this.actorService.sendMessage(message, actor, a);
		}

	}

	public Collection<Message> showMessages() {

		this.actorService.checkUserLogin();

		UserAccount userAccount;
		Actor actor;
		Collection<Message> result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		result = this.findMessagesByActorId(actor.getId());

		return result;
	}

	public Collection<Message> findMessagesByActorId(final int id) {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Collection<Message> result;

		result = this.messageRepository.findMessagesByActorId(id);

		return result;

	}
}
