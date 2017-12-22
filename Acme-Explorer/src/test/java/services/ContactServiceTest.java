
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
import utilities.AbstractTest;
import domain.Contact;
import domain.Explorer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ContactServiceTest extends AbstractTest {

	@Autowired
	public ContactService	contactService;
	@Autowired
	public ExplorerService	explorerService;
	@Autowired
	public ActorService		actorService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		Contact c;
		c = this.contactService.create();

		Assert.notNull(c);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("explorer1");
		final Collection<Contact> contacts = this.contactService.findAll();

		Assert.notEmpty(contacts);
		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("explorer1");
		final Contact c = (Contact) this.contactService.findAll().toArray()[0];
		final int cId = c.getId();

		final Contact ct = this.contactService.findOne(cId);
		Assert.notNull(ct);

		super.unauthenticate();

	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final Contact c = this.contactService.create();

		c.setName("Manolo Padilla");
		c.setEmail("manolopadilla@gmail.com");

		final Contact savedc = this.contactService.save(c);

		Assert.isTrue(this.contactService.findAll().contains(savedc));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("explorer1");

		final Contact c = (Contact) this.contactService.findAll().toArray()[0];
		final Explorer e = this.explorerService.findOne(this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId()).getId());

		Assert.notNull(c);
		Assert.notNull(e);

		this.contactService.delete(c);

		super.unauthenticate();
	}
}
