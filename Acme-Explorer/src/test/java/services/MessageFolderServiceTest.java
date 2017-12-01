
package services;

import java.util.Collection;

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
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MessageFolderServiceTest extends AbstractTest {

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
		super.authenticate("ranger1");
		final MessageFolder messageFolder = this.messageFolderService.create();
		Assert.notNull(messageFolder);
		Assert.isNull(messageFolder.getMessageFolderFather());
		Assert.isTrue(!messageFolder.getIsDefault());
		Assert.isNull(messageFolder.getName());
		Assert.isTrue(messageFolder.getMessageFolderChildren().size() == 0);
		Assert.isTrue(messageFolder.getMessages().size() == 0);
		super.unauthenticate();
	}

	//@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<MessageFolder> messageFolders = this.messageFolderService.findAll();

		Assert.notNull(messageFolders);
		super.unauthenticate();
	}

	//@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final MessageFolder messageFolder = this.messageFolderService.findOne(6565);
		Assert.notNull(messageFolder);
		super.unauthenticate();
	}
	@Test
	public void testSave() {
		super.authenticate("ranger1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		final MessageFolder messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		messageFolder.setMessageFolderFather(null);

		final MessageFolder savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));

		super.unauthenticate();
	}
	@Test
	public void testDelete() {
		super.authenticate("ranger1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());

		final MessageFolder messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		final MessageFolder savedMessageFolder = this.messageFolderService.save(messageFolder);

		this.messageFolderService.delete(savedMessageFolder);
		Assert.isTrue(!this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(!actor.getMessageFolders().contains(savedMessageFolder));

	}

	@Test
	public void testFindMessageFolder() {
		super.authenticate("admin1");
		Assert.notNull(this.messageFolderService.findMessageFolder("in box", (Actor) this.administratorService.findAll().toArray()[0]));
		super.unauthenticate();
	}
}
