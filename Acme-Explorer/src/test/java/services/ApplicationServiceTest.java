
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Application;
import domain.Explorer;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	@Autowired
	public ApplicationService	applicationService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public ExplorerService		explorerService;
	@Autowired
	public TripService			tripService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		final Application application = this.applicationService.create();
		Assert.isNull(application.getCommentaries());
		Assert.notNull(application.getDate());
		Assert.isNull(application.getRejection());
		Assert.isNull(application.getTrip());
		Assert.isTrue(application.getStatus() == "PENDING");

	}
	@Test
	public void testFindAll() {
		super.authenticate("explorer1");
		final Collection<Application> applications = this.applicationService.findAll();

		Assert.notNull(applications);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("explorer1");
		final Application application, foundApplication;
		application = (Application) this.applicationService.findAll().toArray()[0];
		foundApplication = this.applicationService.findOne(application.getId());
		Assert.notNull(foundApplication);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");

		//final UserAccount userAccount = LoginService.getPrincipal();
		final Explorer explorer = (Explorer) this.explorerService.findAll().toArray()[0];
		Assert.notNull(explorer);

		final Trip trip = (Trip) this.tripService.findAll().toArray()[3];

		final Application application = this.applicationService.create();
		application.setTrip(trip);

		final Application savedApplication = this.applicationService.save(application);
		Assert.isTrue(this.applicationService.findAll().contains(savedApplication));
		Assert.isTrue(explorer.getApplications().contains(savedApplication));

		super.unauthenticate();
	}

	@Test
	public void testfindApplicationsByStatus() {
		final Explorer e = (Explorer) this.explorerService.findAll().toArray()[0];
		final Collection<Application> aps = this.applicationService.findApplicationsGroupByStatus(e);
		Assert.notNull(aps);
	}

	@Test
	public void testGetApplicationExplorer() {
		final Explorer e = (Explorer) this.explorerService.findAll().toArray()[1];
		final Trip t = (Trip) this.tripService.findAll().toArray()[3];
		Assert.notNull(e);
		Assert.notNull(t);
		this.applicationService.getApplicationTripExplorer(e.getId(), t.getId());

	}

	@Test
	public void testGetRatioPendingApplications() {
		super.authenticate("admin1");

		String result;

		result = this.applicationService.getRatioPendingApplications();

		Assert.notNull(result);
		Assert.isTrue(!result.equals(""));
	}

	@Test
	public void testGetRatioDueApplications() {
		super.authenticate("admin1");

		String result;

		result = this.applicationService.getRatioDueApplications();

		Assert.notNull(result);
		Assert.isTrue(!result.equals(""));
	}

	@Test
	public void testGetRatioAcceptedApplications() {
		super.authenticate("admin1");

		String result;

		result = this.applicationService.getRatioAcceptedApplications();

		Assert.notNull(result);
		Assert.isTrue(!result.equals(""));
	}

	@Test
	public void testGetRatioCancelledApplications() {
		super.authenticate("admin1");

		String result;

		result = this.applicationService.getRatioCancelledApplications();

		Assert.notNull(result);
		Assert.isTrue(!result.equals(""));
	}

	@Test
	public void testGetApplicationTripExplorer() {
		final Explorer explorer = (Explorer) this.explorerService.findAll().toArray()[1];
		final Trip trip = (Trip) this.tripService.findAll().toArray()[0];
		Application result;

		result = this.applicationService.getApplicationTripExplorer(explorer.getId(), trip.getId());

		Assert.notNull(result);
	}

	@Test
	public void testFindApplicationsGroupByStatus() {
		super.authenticate("explorer1");
		final Explorer explorer = (Explorer) this.explorerService.findAll().toArray()[0];

		Collection<Application> result;

		result = this.applicationService.findApplicationsGroupByStatus(explorer);

		Assert.notNull(result);
		Assert.isTrue(!result.isEmpty());
	}

}
