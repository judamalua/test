
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Search;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SearchServiceTest extends AbstractTest {

	@Autowired
	public SearchService		searchService;
	@Autowired
	public ApplicationService	aplicationService;
	@Autowired
	public ManagerService		managerService;


	@Test
	public void testCreate() {
		super.authenticate("explorer1");
		final Search r = this.searchService.create();
		Assert.isNull(r.getDateRangeEnd());
		Assert.isNull(r.getDateRangeStart());
		Assert.isNull(r.getKeyWord());
		Assert.isNull(r.getPriceRangeEnd());
		Assert.isNull(r.getPriceRangeStart());
		Assert.isTrue(r.getSearchMoment() != null);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("explorer1");
		final Search r = this.searchService.create();
		r.setDateRangeEnd(new Date(System.currentTimeMillis() + 10000));
		r.setDateRangeStart(new Date(System.currentTimeMillis() + 100000));
		r.setKeyWord("Contigo pipo");
		r.setPriceRangeEnd(2000.0);
		r.setPriceRangeStart(20.0);
		final Search saved = this.searchService.save(r);

		Assert.isTrue(this.searchService.findAll().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("explorer1");
		final Search r = this.searchService.create();
		r.setDateRangeEnd(new Date(System.currentTimeMillis() + 10000));
		r.setDateRangeStart(new Date(System.currentTimeMillis() + 100000));
		r.setKeyWord("Contigo pipo");
		r.setPriceRangeEnd(2000.0);
		r.setPriceRangeStart(20.0);
		final Search saved = this.searchService.save(r);
		final int id = saved.getId();
		final Search s = this.searchService.findOne(id);
		Assert.notNull(s);

		super.unauthenticate();
	}
	@Test
	public void testFindAll() {
		super.authenticate("explorer1");
		this.searchService.findAll();
		super.unauthenticate();
	}

	//	@Test
	//	public void testDelete() {
	//		super.authenticate("explorer1");
	//		final Search r = this.searchService.create();
	//		r.setDateRangeEnd(new Date(System.currentTimeMillis() + 10000));
	//		r.setDateRangeStart(new Date(System.currentTimeMillis() + 100000));
	//		r.setKeyWord("Contigo pipo");
	//		r.setPriceRangeEnd(2000.0);
	//		r.setPriceRangeStart(20.0);
	//		final Search saved = this.searchService.save(r, false);
	//		Assert.isTrue(this.searchService.findAll().contains(saved));
	//		this.searchService.delete(saved);
	//		Assert.isTrue(!this.searchService.findAll().contains(saved));
	//		super.unauthenticate();
	//
	//	}
}
