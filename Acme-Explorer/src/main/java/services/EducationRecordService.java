
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EducationRecordRepository;
import domain.Curriculum;
import domain.EducationRecord;

@Service
@Transactional
public class EducationRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private EducationRecordRepository	educationRecordRepository;
	@Autowired
	private CurriculumService			curriculumService;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public EducationRecord create() {
		EducationRecord result;

		result = new EducationRecord();

		return result;
	}

	public Collection<EducationRecord> findAll() {

		Collection<EducationRecord> result;

		Assert.notNull(this.educationRecordRepository);
		result = this.educationRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public EducationRecord findOne(final int educationRecordId) {

		EducationRecord result;

		result = this.educationRecordRepository.findOne(educationRecordId);

		return result;

	}

	public EducationRecord save(final EducationRecord educationRecord) {

		assert educationRecord != null;

		EducationRecord result;
		final Curriculum c = this.curriculumService.findCurriculumByRangerID();

		result = this.educationRecordRepository.save(educationRecord);
		if (c.getEducationRecords().contains(educationRecord))
			c.getEducationRecords().remove(educationRecord);

		c.getEducationRecords().add(result);
		this.curriculumService.save(c);

		return result;

	}

	public void delete(final EducationRecord educationRecord) {

		assert educationRecord != null;
		assert educationRecord.getId() != 0;

		Assert.isTrue(this.educationRecordRepository.exists(educationRecord.getId()));
		final Curriculum curr = this.curriculumService.getCurriculumWithEducation(educationRecord);
		curr.getEducationRecords().remove(educationRecord);
		this.curriculumService.save(curr);
		this.educationRecordRepository.delete(educationRecord);

	}
}
