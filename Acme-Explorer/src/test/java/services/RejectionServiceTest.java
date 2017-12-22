
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.Rejection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RejectionServiceTest extends AbstractTest {

	@Autowired
	public RejectionService		rejectionService;
	@Autowired
	public ApplicationService	applicationService;
	@Autowired
	public ManagerService		managerService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		final Rejection r = this.rejectionService.create();
		Assert.isNull(r.getReason());
		Assert.isNull(r.getApplication());
		//Assert.isNull(r.getManager());
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("manager1");

		//this.rejectionService.delete((Rejection) this.rejectionService.findAll().toArray()[0]);
		final Rejection r = this.rejectionService.create();
		final Application application = (Application) this.applicationService.findAll().toArray()[4];
		r.setReason("El misil termonuclear no es intercontinental");
		this.applicationService.changeStatus(application, "REJECTED");
		r.setApplication(application);

		final Rejection saved = this.rejectionService.save(r);

		Assert.isTrue(this.rejectionService.findAll().contains(saved));
		super.unauthenticate();
	}
	@Test
	public void testFindOne() {
		final Rejection r = (Rejection) this.rejectionService.findAll().toArray()[0];
		Assert.isTrue(r.getReason().equals("Because yes"));
	}
	@Test
	public void testFindAll() {
		Assert.isTrue(this.rejectionService.findAll().size() != 0);
	}

	//	@Test
	//	public void testDelete() {
	//		super.authenticate("explorer1");
	//		//		final Rejection r = this.rejectionService.create();
	//		//		r.setReason("El misil termonuclear no es intercontinental");
	//		//		r.setApplication(this.aplicationService.findOne(6766));
	//		//		r.setManager(this.managerService.findOne(6734));
	//		//		final Rejection saved = this.rejectionService.save(r);
	//		//		Assert.isTrue(this.rejectionService.findAll().contains(saved));
	//		final Rejection r = (Rejection) this.rejectionService.findAll().toArray()[0];
	//		this.rejectionService.delete(r);
	//		Assert.isTrue(!this.rejectionService.findAll().contains(r));
	//		super.unauthenticate();
	//
	//	}
}
