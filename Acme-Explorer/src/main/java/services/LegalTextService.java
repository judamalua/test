
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalTextRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.LegalText;

@Service
@Transactional
public class LegalTextService {

	// Managed repository --------------------------------------------------

	@Autowired
	private LegalTextRepository	legalTextRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods --------------------------------------------------

	public LegalText create() {
		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));
		this.checkUserLogin();

		LegalText result;
		final Collection<String> applicableLaws = new HashSet<String>();

		//		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		//		Date c = null;
		//		try {
		//			c = sdf.parse("25-05-2015");
		//		} catch (final ParseException e) {
		//			e.printStackTrace();
		//		}

		result = new LegalText();
		result.setApplicableLaws(applicableLaws);
		//result.setRegistrationDate(c);
		result.setRegistrationDate(new Date(System.currentTimeMillis() - 2000));

		return result;
	}

	public Collection<LegalText> findAll() {

		Collection<LegalText> result;

		Assert.notNull(this.legalTextRepository);
		result = this.legalTextRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Collection<LegalText> findAllFinalMode() {

		Collection<LegalText> result;

		Assert.notNull(this.legalTextRepository);
		result = this.legalTextRepository.findAllFinalMode();
		Assert.notNull(result);

		return result;

	}

	public LegalText findOne(final int legalTextId) {

		LegalText result;

		result = this.legalTextRepository.findOne(legalTextId);

		return result;

	}

	public LegalText save(final LegalText legalText) {

		this.checkUserLogin();

		assert legalText != null;

		// Filtro para las palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Administrator) {
			this.actorService.checkSpamWords(legalText.getTitle());
			this.actorService.checkSpamWords(legalText.getBody());
			this.actorService.checkSpamWords(legalText.getApplicableLaws());
		}

		// Requirement 14.2: A legal text cannot be edited if it is saved in final mode.
		if (legalText.getId() != 0)
			if (legalText.getFinalMode())
				Assert.isTrue(!this.legalTextRepository.findOne(legalText.getId()).getFinalMode());

		LegalText result;

		result = this.legalTextRepository.save(legalText);

		if (legalText.getId() == 0)
			result.setRegistrationDate(new Date(System.currentTimeMillis() - 2000));
		else
			result.setRegistrationDate(legalText.getRegistrationDate());

		return result;

	}
	public void delete(final LegalText legalText) {
		this.checkUserLogin();

		assert legalText != null;
		assert legalText.getId() != 0;

		// Requirement 14.2: A legal text cannot be deleted if it is saved in final mode.
		Assert.isTrue(!legalText.getFinalMode());

		Assert.isTrue(this.legalTextRepository.exists(legalText.getId()));

		this.legalTextRepository.delete(legalText);

	}

	// Other business methods --------------------------------------------------

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

}
