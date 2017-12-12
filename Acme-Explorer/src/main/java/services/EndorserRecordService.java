
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EndorserRecordRepository;
import domain.Curriculum;
import domain.EndorserRecord;

@Service
@Transactional
public class EndorserRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private EndorserRecordRepository	endorserRecordRepository;

	@Autowired
	private CurriculumService			curriculumService;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public EndorserRecord create() {
		EndorserRecord result;

		result = new EndorserRecord();

		return result;
	}

	public Collection<EndorserRecord> findAll() {

		Collection<EndorserRecord> result;

		Assert.notNull(this.endorserRecordRepository);
		result = this.endorserRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public EndorserRecord findOne(final int endorserRecordId) {

		EndorserRecord result;

		result = this.endorserRecordRepository.findOne(endorserRecordId);

		return result;

	}

	public EndorserRecord save(final EndorserRecord endorserRecord) {

		assert endorserRecord != null;

		EndorserRecord result;
		final Curriculum c = this.curriculumService.findCurriculumByRangerID();

		result = this.endorserRecordRepository.save(endorserRecord);
		if (c.getEndorserRecords().contains(endorserRecord))
			c.getEndorserRecords().remove(endorserRecord);

		c.getEndorserRecords().add(result);
		this.curriculumService.save(c);

		return result;

	}

	public void delete(final EndorserRecord endorserRecord) {

		assert endorserRecord != null;
		assert endorserRecord.getId() != 0;

		Assert.isTrue(this.endorserRecordRepository.exists(endorserRecord.getId()));
		final Curriculum curr = this.curriculumService.getCurriculumWithEndorser(endorserRecord);
		curr.getEndorserRecords().remove(endorserRecord);
		this.curriculumService.save(curr);
		this.endorserRecordRepository.delete(endorserRecord);

	}

}
