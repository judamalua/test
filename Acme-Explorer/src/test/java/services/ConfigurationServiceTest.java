
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	@Autowired
	public ConfigurationService	configurationService;


	@Test
	public void testFindConfiguration() {
		super.authenticate("admin1");

		Assert.notNull(this.configurationService.findConfiguration());

		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Configuration config = this.configurationService.findConfiguration();
		final String banner = "https://banner.com";
		config.setBannerUrl(banner);

		final Configuration savedConfig = this.configurationService.save(config);

		Assert.isTrue(this.configurationService.findConfiguration().getBannerUrl().equals(banner));
		Assert.isTrue(savedConfig.getBannerUrl().equals(banner));

		super.unauthenticate();
	}

}
