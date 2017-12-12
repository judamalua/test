
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TagRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Tag;
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


	// Simple CRUD methods --------------------------------------------------

	public Tag create() {
		this.checkUserLogin();

		Tag result;

		result = new Tag();
		// When we create the object, we initialize its trips to an empty list.
		final Collection<Trip> trips = new HashSet<Trip>();
		result.setTrips(trips);

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

		Tag result, storedTag;

		storedTag = this.tagRepository.findOne(tag.getId());
		// Requirement 14.3: A tag name can only be modified if it is not referenced by any trip.
		if (tag.getId() != 0 && !storedTag.getName().equals(tag.getName()))
			Assert.isTrue(tag.getTrips().size() == 0);

		result = this.tagRepository.save(tag);

		for (final Trip t : tag.getTrips()) {
			if (!t.getTags().contains(tag))
				t.getTags().remove(tag);
			t.getTags().add(result);
			this.tripService.save(t);
		}

		return result;

	}
	public void delete(final Tag tag) {
		this.checkUserLogin();

		assert tag != null;
		assert tag.getId() != 0;

		Assert.isTrue(this.tagRepository.exists(tag.getId()));
		if (tag.getTrips().size() != 0)
			for (final Trip t : tag.getTrips()) {
				t.getTags().remove(tag);
				this.tripService.save(t);
			}

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
