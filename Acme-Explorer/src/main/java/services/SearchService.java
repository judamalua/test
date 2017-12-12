
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SearchRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Search;

@Service
@Transactional
public class SearchService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SearchRepository	searchRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods --------------------------------------------------
	public Search create() {
		this.checkUserLogin();

		Search result;

		result = new Search();

		result.setSearchMoment(new Date(System.currentTimeMillis() - 2000));

		return result;
	}

	public Collection<Search> findAll() {
		this.checkUserLogin();

		Collection<Search> result;

		Assert.notNull(this.searchRepository);
		result = this.searchRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Search findOne(final int searchId) {
		this.checkUserLogin();

		Search result;

		result = this.searchRepository.findOne(searchId);

		return result;

	}

	public Search save(final Search search) {
		this.checkUserLogin();

		assert search != null;

		final Search result;
		search.setSearchMoment(new Date(System.currentTimeMillis() - 2000));

		result = this.searchRepository.save(search);

		return result;

	}

	public void delete(final Search search) {
		this.checkUserLogin();

		assert search != null;
		assert search.getId() != 0;

		Assert.isTrue(this.searchRepository.exists(search.getId()));

		this.searchRepository.delete(search);

	}
	// Other Business  methods --------------------------------------------------

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

}
