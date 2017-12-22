
package services;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public MessageService		messageService;
	@Autowired
	public MessageFolderService	messageFolderService;
	@Autowired
	public AdministratorService	administratorService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Message message;
		message = this.messageService.create();

		Assert.notNull(message);
		Assert.isNull(message.getBody());
		Assert.isNull(message.getPriority());
		Assert.isNull(message.getReceiver());
		super.unauthenticate();

	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<Message> messages = this.messageService.findAll();

		Assert.notNull(messages);

	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final Message message = (Message) this.messageService.findAll().toArray()[0];

		final Message result = this.messageService.findOne(message.getId());
		Assert.notNull(result);
	}
	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());

		final MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		final Message message = this.messageService.create();
		message.setBody("Body 1");
		message.setMessageFolder(messageFolder);
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(actor);
		message.setSubject("Subject 1");

		final Message savedMessage = this.messageService.save(message);
		Assert.isTrue(this.messageService.findAll().contains(savedMessage));
		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));
		Assert.isTrue(savedMessage.getMessageFolder().getMessages().contains(savedMessage));
		super.unauthenticate();
	}

	@Test
	public void deleteMessage() {
		super.authenticate("admin1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		this.messageFolderService.save(messageFolder);
		final Message message = (Message) messageFolder.getMessages().toArray()[0];
		Assert.notNull(message);
		this.messageService.delete(message);
		Assert.isTrue(message.getMessageFolder().getName().equals("trash box") || !this.messageFolderService.findAll().contains(message));
		messageFolder = this.messageFolderService.findOne(messageFolder.getId());
		Assert.isTrue(!messageFolder.getMessages().contains(message));

	}
	@Test
	public void moveMessage() {
		super.authenticate("admin1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		MessageFolder messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		this.messageFolderService.save(messageFolder);
		Message message = (Message) messageFolder.getMessages().toArray()[0];
		final MessageFolder trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
		Assert.notNull(message);
		message = this.actorService.moveMessage(message, trashBox);

		messageFolder = this.messageFolderService.findOne(messageFolder.getId());
		Assert.isTrue(!messageFolder.getMessages().contains(message));
		//Assert.isTrue(trashBox.getMessages().contains(message));
		super.unauthenticate();
	}

	@Test
	public void testBroadcastNotification() {
		super.authenticate("admin1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		final MessageFolder messageFolder = this.messageFolderService.findMessageFolder("notification box", actor);
		final Message message = this.messageService.create();
		message.setMessageFolder(messageFolder);
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(actor);
		message.setSubject("Subject 1");
		this.messageService.broadcastNotification(message);
		Boolean hasIt = false;
		final Collection<Message> messages = this.messageFolderService.findMessageFolder("notification box", ((Actor) this.administratorService.findAll().toArray()[0])).getMessages();
		for (final Message m : messages)
			if (m.getBody() == "Body 1")
				hasIt = true;
		Assert.isTrue(hasIt);
		super.unauthenticate();
	}

	@Test
	public void testFindByActorId() {
		super.authenticate("admin1");
		final int id = ((Actor) this.administratorService.findAll().toArray()[0]).getId();
		Assert.notNull(this.messageService.findMessagesByActorId(id));
		super.unauthenticate();
	}
}
