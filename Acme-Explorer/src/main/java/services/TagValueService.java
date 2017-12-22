
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TagValueRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Manager;
import domain.TagValue;
import domain.Trip;

@Service
@Transactional
public class TagValueService {

	// Managed repository --------------------------------------------------

	@Autowired
	private TagValueRepository	tagValueRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;
	@Autowired
	private TripService			tripService;


	// Simple CRUD methods --------------------------------------------------

	public TagValue create() {
		this.checkUserLogin();

		TagValue result;

		result = new TagValue();

		//		final Collection<Trip> trips = new HashSet<Trip>();
		//		result.setTrips(trips);

		return result;
	}

	public Collection<TagValue> findAll() {
		Collection<TagValue> result;

		Assert.notNull(this.tagValueRepository);
		result = this.tagValueRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public TagValue findOne(final int tagValueId) {

		TagValue result;

		result = this.tagValueRepository.findOne(tagValueId);

		return result;

	}
	public TagValue save(final TagValue tagValue, final Trip trip) {
		this.checkUserLogin();

		assert tagValue != null;
		Assert.notNull(trip);

		TagValue result;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		// Comprobación palabras de spam
		if (actor instanceof Manager)
			this.actorService.checkSpamWords(tagValue.getValue());

		result = this.tagValueRepository.save(tagValue);

		if (trip.getTagValues().contains(tagValue))
			trip.getTagValues().remove(tagValue);
		trip.getTagValues().add(result);
		this.tripService.save(trip);

		return result;

	}
	public void delete(final TagValue tagValue) {
		this.checkUserLogin();

		assert tagValue != null;
		assert tagValue.getId() != 0;
		Assert.isTrue(this.tagValueRepository.exists(tagValue.getId()));

		Trip trip;

		trip = this.tripService.findTripByTagValue(tagValue.getId());
		trip.getTagValues().remove(tagValue);

		this.tripService.save(trip);

		this.tagValueRepository.delete(tagValue);

	}

	public Collection<TagValue> findTagValuesByTagId(final int tagId) {

		Collection<TagValue> result;

		result = this.tagValueRepository.findTagValuesByTagId(tagId);

		return result;
	}

	// Other business methods --------------------------------------------------

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

}
