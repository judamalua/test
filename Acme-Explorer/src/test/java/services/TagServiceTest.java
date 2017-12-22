
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TagServiceTest extends AbstractTest {

	@Autowired
	public TagService	tagService;
	@Autowired
	public TripService	tripService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Tag tag;
		tag = this.tagService.create();

		Assert.notNull(tag);
		Assert.isNull(tag.getName());

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {

		final Collection<Tag> tags = this.tagService.findAll();

		Assert.notNull(tags);

	}

	@Test
	public void testFindOne() {
		final Tag tag1 = (Tag) this.tagService.findAll().toArray()[1];
		final int tagId = tag1.getId();

		final Tag tag = this.tagService.findOne(tagId);
		Assert.notNull(tag);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Tag tag = this.tagService.create();

		tag.setName("Name");

		final Tag savedTag = this.tagService.save(tag);

		Assert.isTrue(this.tagService.findAll().contains(savedTag));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("admin1");

		final Tag tag = (Tag) this.tagService.findAll().toArray()[1];

		this.tagService.delete(tag);

		super.unauthenticate();
	}
}
