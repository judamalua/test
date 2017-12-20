
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.StoryRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Explorer;
import domain.Story;
import domain.Trip;

@Service
@Transactional
public class StoryService {

	// Managed repository --------------------------------------------------

	@Autowired
	private StoryRepository	storyRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private TripService		tripService;
	@Autowired
	private ActorService	actorService;

	//	@Autowired
	//	private ApplicationService	applicationService;
	@Autowired
	private ExplorerService	explorerService;


	// Simple CRUD methods --------------------------------------------------

	public Story create() {
		this.actorService.checkUserLogin();
		Story result;
		final Collection<String> attachments = new HashSet<String>();

		result = new Story();
		result.setAttachments(attachments);

		return result;
	}

	public Collection<Story> findAll() {

		Collection<Story> result;

		Assert.notNull(this.storyRepository);
		result = this.storyRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Story findOne(final int storyId) {

		Story result;

		result = this.storyRepository.findOne(storyId);

		return result;

	}

	public Story save(final Story story, final Trip trip) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Explorer) {
			this.actorService.checkSpamWords(story.getTitle());
			this.actorService.checkSpamWords(story.getPieceOfText());
			this.actorService.checkSpamWords(story.getAttachments());
		}

		assert story != null;
		Story result;
		Explorer writer;
		Date currentDate;
		Collection<Trip> acceptedTrips;

		writer = (Explorer) actor;
		currentDate = new Date();
		acceptedTrips = this.tripService.getAcceptedTripsFromExplorerId(writer.getId());

		// Requisito funcional 44.2
		Assert.isTrue(acceptedTrips.contains(trip));
		Assert.isTrue(trip.getEndDate().before(currentDate));

		result = this.storyRepository.save(story);
		if (story.getId() != 0) {
			writer.getStories().remove(story);
			trip.getStories().remove(story);
		}

		writer.getStories().add(result);
		this.explorerService.save(writer);
		trip.getStories().add(result);
		this.tripService.save(trip);

		return result;

	}
	//	public void delete(final Story story) {
	//
	//		final UserAccount userAccount = LoginService.getPrincipal();
	//		Assert.notNull(userAccount);
	//		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
	//		Assert.notNull(actor);
	//
	//		Explorer writer;
	//		Trip trip;
	//
	//		assert story != null;
	//		assert story.getId() != 0;
	//
	//		writer = (Explorer) actor;
	//		trip = this.tripService.getTripFromStory(story.getId());
	//
	//		Assert.isTrue(this.storyRepository.exists(story.getId()));
	//
	//		writer.getStories().remove(story);
	//		this.explorerService.save(writer);
	//
	//		trip.getStories().remove(story);
	//		this.tripService.save(trip);
	//
	//		this.storyRepository.delete(story);
	//
	//	}

	// Other Business Methods --------------------------------------------------

}
