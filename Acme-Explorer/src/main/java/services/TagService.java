
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TagRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@Service
@Transactional
public class TagService {

	// Managed repository --------------------------------------------------

	@Autowired
	private TagRepository	tagRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService	actorService;
	@Autowired
	private TripService		tripService;
	@Autowired
	private TagValueService	tagValueService;


	// Simple CRUD methods --------------------------------------------------

	public Tag create() {
		this.checkUserLogin();

		Tag result;

		result = new Tag();

		//		final Collection<Trip> trips = new HashSet<Trip>();
		//		result.setTrips(trips);

		return result;
	}

	public Collection<Tag> findAll() {
		Collection<Tag> result;

		Assert.notNull(this.tagRepository);
		result = this.tagRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Tag findOne(final int tagId) {

		Tag result;

		result = this.tagRepository.findOne(tagId);

		return result;

	}
	public Tag save(final Tag tag) {
		this.checkUserLogin();

		assert tag != null;

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Administrator)
			this.actorService.checkSpamWords(tag.getName());

		Tag result;
		Trip trip;
		final Collection<TagValue> tagValues;

		tagValues = this.tagValueService.findTagValuesByTagId(tag.getId());

		// Requirement 14.3: A tag name can only be modified if it is not referenced by any trip.
		//		if (tag.getId() != 0 && !storedTag.getName().equals(tag.getName()))
		//			Assert.isTrue(trips.size() == 0);

		result = this.tagRepository.save(tag);

		if (tagValues != null)
			for (final TagValue tv : tagValues) {
				tv.setTag(result);
				trip = this.tripService.findTripByTagValue(tv.getId());
				this.tagValueService.save(tv, trip);
			}
		return result;

	}
	public void delete(final Tag tag) {
		this.checkUserLogin();

		assert tag != null;
		assert tag.getId() != 0;

		Collection<TagValue> tagValues;

		//		Collection<Trip> trips;

		//		trips = this.tripService.findTripsByTagId(tag.getId());

		Assert.isTrue(this.tagRepository.exists(tag.getId()));

		tagValues = this.tagValueService.findTagValuesByTagId(tag.getId());

		for (final TagValue tagValue : tagValues)
			this.tagValueService.delete(tagValue);

		this.tagRepository.delete(tag);

	}

	// Other business methods --------------------------------------------------

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

	public Collection<Tag> findTagsByTrip(final int tripId) {

		Collection<Tag> result;

		result = this.tagRepository.findTagsByTrip(tripId);

		return result;
	}

}
