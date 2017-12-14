
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SearchRepository;
import domain.Explorer;
import domain.Search;

@Service
@Transactional
public class SearchService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SearchRepository	searchRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private ExplorerService		explorerservice;
	@Autowired
	private ActorService		actorservice;


	// Simple CRUD methods --------------------------------------------------
	public Search create() {

		Search result;

		result = new Search();

		result.setSearchMoment(new Date(System.currentTimeMillis() - 2000));
		result.setmillis(LocalDateTime.now().getMillisOfSecond());

		return result;
	}

	public Collection<Search> findAll() {
		this.checkSearchDataBase();
		Collection<Search> result;

		Assert.notNull(this.searchRepository);
		result = this.searchRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Search findOne(final int searchId) {
		this.checkSearchDataBase();

		Search result;

		result = this.searchRepository.findOne(searchId);

		return result;

	}

	public Search save(final Search search) {

		assert search != null;

		final Search result;
		search.setSearchMoment(new Date(System.currentTimeMillis() - 2000));
		search.setmillis(LocalDateTime.now().getMillisOfSecond());
		result = this.searchRepository.save(search);

		final Explorer e = (Explorer) this.actorservice.findActorByPrincipal();
		e.getSearches().add(result);
		this.explorerservice.save(e);

		return result;

	}

	public void delete(final Search search) {

		assert search != null;
		assert search.getId() != 0;

		final Explorer e = this.explorerservice.findExplorerWithSearch(search);
		Assert.notNull(e);
		e.getSearches().remove(search);
		this.explorerservice.save(e);

		this.searchRepository.delete(search);

	}
	// Other Business  methods --------------------------------------------------

	public void checkSearchDataBase() {
		final Collection<Search> searchs = this.searchRepository.findAll();
		long diferencia;
		for (final Search s : searchs) {
			final Date d = new Date();
			diferencia = d.getTime() - s.getSearchMoment().getTime();
			if (diferencia >= 3600000)
				this.delete(s);

		}
	}
}
