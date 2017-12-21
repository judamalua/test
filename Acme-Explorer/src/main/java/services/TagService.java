
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
		//		final Collection<TagValue> tagValues;

		//		tagValues = tag.getTagValues();

		// Requirement 14.3: A tag name can only be modified if it is not referenced by any trip.
		//		if (tag.getId() != 0 && !storedTag.getName().equals(tag.getName()))
		//			Assert.isTrue(trips.size() == 0);

		result = this.tagRepository.save(tag);

		//		for (final TagValue tv : tagValues) {
		//						if (!tag.getTagValues().contains(tv))
		//							t.getTags().remove(tag);
		//			tag.getTagValues().add(result);
		//			this.tripService.save(t);
		//		}

		return result;

	}
	public void delete(final Tag tag) {
		this.checkUserLogin();

		assert tag != null;
		assert tag.getId() != 0;

		//		Collection<Trip> trips;

		//		trips = this.tripService.findTripsByTagId(tag.getId());

		Assert.isTrue(this.tagRepository.exists(tag.getId()));
		//		if (trips.size() != 0)
		//			for (final Trip t : trips) {
		//				t.getTags().remove(tag);
		//				this.tripService.save(t);
		//			}

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
