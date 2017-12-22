
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
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TagValueServiceTest extends AbstractTest {

	@Autowired
	public TagValueService	tagValueService;
	@Autowired
	public TripService		tripService;
	@Autowired
	public TagService		tagService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		TagValue tagvalue;
		tagvalue = this.tagValueService.create();

		Assert.notNull(tagvalue);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {

		final Collection<TagValue> tagvalues = this.tagValueService.findAll();

		Assert.notNull(tagvalues);

	}

	@Test
	public void testFindOne() {
		final TagValue tagvalue = (TagValue) this.tagValueService.findAll().toArray()[0];
		final int tagvalueId = tagvalue.getId();

		final TagValue tagvalue1 = this.tagValueService.findOne(tagvalueId);
		Assert.notNull(tagvalue1);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final TagValue tagvalue = (TagValue) this.tagValueService.findAll().toArray()[0];
		tagvalue.setValue("value23");

		final Trip t = (Trip) this.tripService.findAll().toArray()[1];
		tagvalue.setValue("value10");

		final TagValue savedTag = this.tagValueService.save(tagvalue, t);
		final int id = savedTag.getId();
		Assert.notNull(this.tagValueService.findOne(id));

		super.unauthenticate();
	}
	@Test
	public void testDelete() {
		super.authenticate("admin1");

		final TagValue tagvalue = (TagValue) this.tagValueService.findAll().toArray()[0];
		Trip trip;
		Tag tag;

		trip = this.tripService.findTripByTagValue(tagvalue.getId());
		tag = tagvalue.getTag();

		Assert.notNull(tag);
		Assert.notNull(trip);

		this.tagValueService.delete(tagvalue);

		Assert.isTrue(!this.tagValueService.findAll().contains(tagvalue));
		Assert.isTrue(!this.tripService.findOne(trip.getId()).getTagValues().contains(tagvalue));

		super.unauthenticate();
	}
}
