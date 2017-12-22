
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Curriculum;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Managed repository --------------------------------------------------

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	@Autowired
	private CurriculumService				curriculumService;

	@Autowired
	private ActorService					actorService;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public MiscellaneousRecord create() {
		this.actorService.checkUserLogin();
		MiscellaneousRecord result;

		result = new MiscellaneousRecord();

		return result;
	}

	public Collection<MiscellaneousRecord> findAll() {

		Collection<MiscellaneousRecord> result;

		Assert.notNull(this.miscellaneousRecordRepository);
		result = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public MiscellaneousRecord findOne(final int miscellaneousRecordId) {

		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);

		return result;

	}

	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		this.actorService.checkUserLogin();

		assert miscellaneousRecord != null;

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(miscellaneousRecord.getTitle());
		if (miscellaneousRecord.getAttachment() != null)
			this.actorService.checkSpamWords(miscellaneousRecord.getAttachment());

		if (miscellaneousRecord.getCommentaries() != null)
			this.actorService.checkSpamWords(miscellaneousRecord.getCommentaries());

		MiscellaneousRecord result;
		final Curriculum c = this.curriculumService.findCurriculumByRangerID();

		result = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		if (c.getMiscellaneousRecords().contains(miscellaneousRecord))
			c.getMiscellaneousRecords().remove(miscellaneousRecord);

		c.getMiscellaneousRecords().add(result);
		this.curriculumService.save(c);

		return result;

	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		this.actorService.checkUserLogin();

		assert miscellaneousRecord != null;
		assert miscellaneousRecord.getId() != 0;

		Assert.isTrue(this.miscellaneousRecordRepository.exists(miscellaneousRecord.getId()));
		final Curriculum curr = this.curriculumService.getCurriculumWithMiscellaneous(miscellaneousRecord);
		curr.getMiscellaneousRecords().remove(miscellaneousRecord);
		this.curriculumService.save(curr);
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);

	}
}
