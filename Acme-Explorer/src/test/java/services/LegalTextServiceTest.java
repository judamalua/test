
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

import utilities.AbstractTest;
import domain.LegalText;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class LegalTextServiceTest extends AbstractTest {

	@Autowired
	public LegalTextService	legalTextService;


	@Test
	public void testCreate() {
		//		super.authenticate("legalText1");
		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		//		Assert.notNull(actor);
		super.authenticate("admin1");
		final LegalText legalText = this.legalTextService.create();
		Assert.isNull(legalText.getTitle());
		Assert.isNull(legalText.getBody());
		Assert.notNull(legalText.getRegistrationDate());
		Assert.isTrue(!legalText.getFinalMode());
		Assert.isTrue(legalText.getApplicableLaws().isEmpty());
		super.unauthenticate();

	}
	@Test
	public void testFindAll() {

		final Collection<LegalText> legalTexts = this.legalTextService.findAll();

		Assert.notNull(legalTexts);
		//super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final LegalText legalText1 = (LegalText) this.legalTextService.findAll().toArray()[0];
		final int id = legalText1.getId();
		final LegalText legalText = this.legalTextService.findOne(id);
		Assert.notNull(legalText);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final LegalText legalText = this.legalTextService.create();
		legalText.setTitle("title");
		legalText.setBody("Body");
		legalText.setRegistrationDate(new Date(System.currentTimeMillis() - 1));

		final LegalText savedLegalText = this.legalTextService.save(legalText);

		Assert.isTrue(this.legalTextService.findAll().contains(savedLegalText));
		super.unauthenticate();
	}
	@Test
	public void testDelete() {
		//		super.authenticate("admin1");
		super.authenticate("admin1");
		final LegalText legalText = (LegalText) this.legalTextService.findAll().toArray()[2];

		this.legalTextService.delete(legalText);
		super.unauthenticate();
		//		super.unauthenticate();
	}
}
