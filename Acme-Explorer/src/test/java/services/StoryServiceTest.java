
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Explorer;
import domain.Story;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class StoryServiceTest extends AbstractTest {

	@Autowired
	public ActorService		actorService;
	@Autowired
	public StoryService		storyService;
	@Autowired
	public ExplorerService	explorerService;
	@Autowired
	public TripService		tripService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		Story story;
		story = this.storyService.create();

		Assert.notNull(story);
		Assert.isNull(story.getTitle());
		Assert.isNull(story.getPieceOfText());
		Assert.isTrue(story.getAttachments().size() == 0);

		super.unauthenticate();

	}

	@Test
	public void testFindAll() {

		final Collection<Story> stories = this.storyService.findAll();

		Assert.notNull(stories);

	}

	@Test
	public void testFindOne() {
		final Story story1 = (Story) this.storyService.findAll().toArray()[1];
		final int storyId = story1.getId();

		final Story story = this.storyService.findOne(storyId);
		Assert.notNull(story);
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final Story story = this.storyService.create();
		final Explorer writer = (Explorer) this.explorerService.findAll().toArray()[0];
		final Trip trip = (Trip) this.tripService.findAll().toArray()[2];
		final Collection<String> attachments = new HashSet<String>();
		attachments.add("http://www.aaa.com");

		story.setTitle("Título 1");
		story.setPieceOfText("Texto 1");
		story.setAttachments(attachments);

		final Story savedStory = this.storyService.save(story, trip);

		Assert.isTrue(this.storyService.findAll().contains(savedStory));
		Assert.isTrue(writer.getStories().contains(savedStory));
		Assert.isTrue(trip.getStories().contains(savedStory));

		super.unauthenticate();
	}

}
