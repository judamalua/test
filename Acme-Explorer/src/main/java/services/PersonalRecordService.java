
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PersonalRecordRepository;
import domain.PersonalRecord;

@Service
@Transactional
public class PersonalRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService				actorService;

	//	@Autowired
	//	private CurriculumService			curriculumService;

	@Autowired
	private ConfigurationService		configurationService;


	// Simple CRUD methods --------------------------------------------------

	public PersonalRecord create() {
		this.actorService.checkUserLogin();

		PersonalRecord result;

		result = new PersonalRecord();

		return result;
	}

	public Collection<PersonalRecord> findAll() {

		Collection<PersonalRecord> result;

		Assert.notNull(this.personalRecordRepository);
		result = this.personalRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public PersonalRecord findOne(final int personalRecordId) {

		PersonalRecord result;

		result = this.personalRecordRepository.findOne(personalRecordId);

		return result;

	}

	public PersonalRecord save(final PersonalRecord personalRecord) {

		this.actorService.checkUserLogin();

		assert personalRecord != null;

		PersonalRecord result;
		//final Curriculum c;
		String phoneNumberPrefix;

		phoneNumberPrefix = this.configurationService.findConfiguration().getDefaultPhoneCountryCode();

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(personalRecord.getNameOfCandidate());
		this.actorService.checkSpamWords(personalRecord.getPhoto());
		this.actorService.checkSpamWords(personalRecord.getEmail());
		this.actorService.checkSpamWords(personalRecord.getLinkedInProfileURL());

		// Si el número de teléfono no tiene prefijo, se añade el de configuración por defecto.
		if (!personalRecord.getPhoneNumber().trim().startsWith("+")) {
			String trimmedPhoneNumber;
			String finalPhoneNumber;

			trimmedPhoneNumber = personalRecord.getPhoneNumber().trim();

			finalPhoneNumber = phoneNumberPrefix + " " + trimmedPhoneNumber;

			personalRecord.setPhoneNumber(finalPhoneNumber);

		}

		//c = this.curriculumService.findCurriculumByRangerID();
		result = this.personalRecordRepository.save(personalRecord);

		//c.setPersonalRecord(result);

		return result;

	}

}
