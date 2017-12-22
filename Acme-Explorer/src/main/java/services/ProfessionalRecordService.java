
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProfessionalRecordRepository;
import domain.Curriculum;
import domain.ProfessionalRecord;

@Service
@Transactional
public class ProfessionalRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ProfessionalRecordRepository	professionalRecordRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService					actorService;
	@Autowired
	private CurriculumService				curriculumService;


	// Simple CRUD methods --------------------------------------------------

	public ProfessionalRecord create() {
		this.actorService.checkUserLogin();

		ProfessionalRecord result;

		result = new ProfessionalRecord();

		return result;
	}

	public Collection<ProfessionalRecord> findAll() {

		Collection<ProfessionalRecord> result;

		Assert.notNull(this.professionalRecordRepository);
		result = this.professionalRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public ProfessionalRecord findOne(final int professionalRecordId) {

		ProfessionalRecord result;

		result = this.professionalRecordRepository.findOne(professionalRecordId);

		return result;

	}

	public ProfessionalRecord save(final ProfessionalRecord professionalRecord) {
		this.actorService.checkUserLogin();

		assert professionalRecord != null;

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(professionalRecord.getCompanyName());
		this.actorService.checkSpamWords(professionalRecord.getRole());
		if (professionalRecord.getAttachment() != null)
			this.actorService.checkSpamWords(professionalRecord.getAttachment());
		if (professionalRecord.getCommentaries() != null)
			this.actorService.checkSpamWords(professionalRecord.getCommentaries());

		ProfessionalRecord result;
		final Curriculum c = this.curriculumService.findCurriculumByRangerID();

		result = this.professionalRecordRepository.save(professionalRecord);
		if (c.getProfessionalRecords().contains(professionalRecord))
			c.getProfessionalRecords().remove(professionalRecord);

		c.getProfessionalRecords().add(result);
		this.curriculumService.save(c);

		return result;

	}

	public void delete(final ProfessionalRecord professionalRecord) {

		assert professionalRecord != null;
		assert professionalRecord.getId() != 0;

		Assert.isTrue(this.professionalRecordRepository.exists(professionalRecord.getId()));
		final Curriculum curr = this.curriculumService.getCurriculumWithProfessional(professionalRecord);
		curr.getProfessionalRecords().remove(professionalRecord);
		this.curriculumService.save(curr);
		this.professionalRecordRepository.delete(professionalRecord);

	}
}
