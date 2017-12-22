
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
import domain.Category;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	public CategoryService	categoryService;
	@Autowired
	public TripService		tripService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Category c;
		c = this.categoryService.create();

		Assert.notNull(c);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");

		final Collection<Category> cs = this.categoryService.findAll();

		Assert.notNull(cs);
		super.unauthenticate();

	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");

		final Category c = (Category) this.categoryService.findAll().toArray()[1];
		final int cId = c.getId();

		final Category ca = this.categoryService.findOne(cId);
		Assert.notNull(ca);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Category c = (Category) this.categoryService.findAll().toArray()[1];

		final Collection<Trip> trips = this.tripService.findTrips(c);
		c.setName("Montaña");

		final Category savedc = this.categoryService.save(c);

		for (final Trip t : trips)
			Assert.isTrue(t.getCategory().equals(savedc));

		Assert.isTrue(this.categoryService.findAll().contains(savedc));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("admin1");

		final Category c = (Category) this.categoryService.findAll().toArray()[2];
		Assert.notNull(c);
		Assert.notNull(c.getCategories());
		final Collection<Trip> t = this.tripService.findTripsByCategoryId(c.getId());
		Assert.notNull(t);

		this.categoryService.delete(c);

		Assert.isTrue(!this.categoryService.findAll().contains(c));
		for (final Trip trip : t)
			Assert.isTrue(!trip.getCategory().equals(c));

		super.unauthenticate();
	}
}
