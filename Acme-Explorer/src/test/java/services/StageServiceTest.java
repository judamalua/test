
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Stage;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class StageServiceTest extends AbstractTest {

	@Autowired
	public StageService	stageService;
	@Autowired
	public TripService	tripService;


	@Test
	public void testCreate() {
		Stage stage;
		stage = this.stageService.create();

		Assert.notNull(stage);
		Assert.isNull(stage.getTitle());
		Assert.isNull(stage.getDescription());
		Assert.isTrue(stage.getPrice() == 0.0);

	}

	@Test
	public void testFindAll() {

		final Collection<Stage> stages = this.stageService.findAll();

		Assert.notNull(stages);

	}

	@Test
	public void testFindOne() {
		final Stage stage1 = (Stage) this.stageService.findAll().toArray()[1];
		final int stageId = stage1.getId();

		final Stage stage = this.stageService.findOne(stageId);
		Assert.notNull(stage);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Stage stage = this.stageService.create();
		final Trip trip = (Trip) this.tripService.findAll().toArray()[0];
		final int numStageBefore = trip.getStages().size();
		stage.setTitle("Stage title");
		stage.setDescription("Description stage 1");
		stage.setPrice(100.0);

		trip.getStages().add(stage);
		this.tripService.save(trip);

		Assert.isTrue(numStageBefore < trip.getStages().size());

		super.unauthenticate();
	}

	@Test
	public void testDelete() {

		super.authenticate("admin1");
		final Stage stage = (Stage) this.stageService.findAll().toArray()[2];
		final Trip trip = this.tripService.getTripFromStageId(stage.getId());

		Assert.notNull(stage);
		Assert.notNull(trip);

		this.stageService.delete(stage);

		Assert.isTrue(!this.stageService.findAll().contains(stage));
		Assert.isTrue(!trip.getStages().contains(stage));

		super.unauthenticate();
	}
}
