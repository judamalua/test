
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

		result = this.personalRecordRepository.save(personalRecord);

		return result;

	}

}
