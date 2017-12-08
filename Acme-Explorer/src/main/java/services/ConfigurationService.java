
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ConfigurationRepository	configurationRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------
	public Configuration create() {
		this.actorService.checkUserLogin();
		Assert.isTrue(this.configurationRepository.findAll().size() == 0);
		Configuration result;

		result = new Configuration();

		return result;
	}

	//	public Collection<Configuration> findAll() {
	//
	//		Collection<Configuration> result;
	//
	//		Assert.notNull(this.configurationRepository);
	//		result = this.configurationRepository.findAll();
	//		Assert.notNull(result);
	//
	//		return result;
	//
	//	}

	public Configuration findConfiguration() {
		Configuration result;

		if (this.configurationRepository.findAll().isEmpty())
			result = null;
		else
			result = this.configurationRepository.findAll().get(0);
		return result;

	}

	public Configuration save(final Configuration configuration) {
		this.actorService.checkUserLogin();
		assert configuration != null;

		Configuration result;

		result = this.configurationRepository.save(configuration);

		return result;

	}

	//	public void checkSpam(final Object o) {
	//		for (int i = 0; i < o.getClass().getMethods().length; i++)
	//			if (o.getClass().getMethods()[i].getReturnType().getName().equals("String")) {
	//				String s = "";
	//				try {
	//					s = (String) o.getClass().getMethods()[i].invoke(o);
	//				} catch (final IllegalAccessException e) {
	//				} catch (final IllegalArgumentException e) {
	//				} catch (final InvocationTargetException e) {
	//				} catch (final SecurityException e) {
	//				}
	//				Boolean badActor = false;
	//				for (final String p : this.findConfiguration().getSpamWords())
	//					if (s.toLowerCase().contains(p.toLowerCase()))
	//						badActor = true;
	//				if (badActor == true) {
	//					final Actor a = this.actorService.findActorByUserAccountId(LoginService.getPrincipal().getId());
	//					a.setSuspicious(true);
	//					this.actorService.save(a);
	//				}
	//			}
	//	}
	//	public void delete(final Configuration configuration) {
	//
	//		assert configuration != null;
	//		assert configuration.getId() != 0;
	//
	//		Assert.isTrue(this.configurationRepository.exists(configuration.getId()));
	//
	//		this.configurationRepository.delete(configuration);
	//
	//	}

	public String getBannerUrl() {
		String result;

		result = this.findConfiguration().getBannerUrl();
		Assert.notNull(result);

		return result;
	}

}
