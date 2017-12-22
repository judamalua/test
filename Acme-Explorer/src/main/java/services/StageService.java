
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.StageRepository;
import domain.Manager;
import domain.Stage;
import domain.Trip;

@Service
@Transactional
public class StageService {

	// Managed repository --------------------------------------------------

	@Autowired
	private StageRepository	stageRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private TripService		tripService;

	@Autowired
	private ActorService	actorService;


	// Simple CRUD methods --------------------------------------------------

	public Stage create() {
		Stage result;

		result = new Stage();

		return result;
	}

	public Collection<Stage> findAll() {

		Collection<Stage> result;

		Assert.notNull(this.stageRepository);
		result = this.stageRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Stage findOne(final int stageId) {

		Stage result;

		result = this.stageRepository.findOne(stageId);

		return result;

	}

	public Stage save(final Stage stage) {

		assert stage != null;
		Stage result;

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Manager) {
			this.actorService.checkSpamWords(stage.getTitle());
			this.actorService.checkSpamWords(stage.getDescription());
		}

		result = this.stageRepository.save(stage);

		return result;

	}

	public void delete(final Stage stage) {
		Trip trip;

		assert stage != null;
		assert stage.getId() != 0;
		trip = this.tripService.getTripFromStageId(stage.getId());

		Assert.notNull(trip);
		Assert.isTrue(trip.getStages().size() != 1);
		Assert.isTrue(this.stageRepository.exists(stage.getId()));

		trip.getStages().remove(stage);
		this.tripService.save(trip);

		this.stageRepository.delete(stage);

	}

}
