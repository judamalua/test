
package services;

import java.util.ArrayList;
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
	private SearchRepository		searchRepository;

	// Supporting services -------------------------------------------------
	//	@Autowired
	//	private CacheService			cacheService;
	@Autowired
	private ExplorerService			explorerservice;
	@Autowired
	private ActorService			actorservice;
	@Autowired
	private ConfigurationService	configurationService;


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

		Search result;

		result = this.searchRepository.findOne(searchId);

		return result;

	}

	public Search save(final Search se, final Boolean isOnCache) {
		//		this.cacheService.checkCache();
		//
		//		assert se != null;
		//
		//		final Search result;
		//
		//		//solo se hace si la busqueda se encuentra en la cach�///////////////////////////////////
		//		if (isOnCache) {
		//			final Collection<Search> searchesInTheSystem = this.findAll();
		//
		//			for (final Search s1 : searchesInTheSystem)
		//				if (s1.getKeyWord().equals(se.getKeyWord()))
		//					if (s1.getPriceRangeStart().equals(se.getPriceRangeStart()))
		//						if (s1.getPriceRangeEnd().equals(se.getPriceRangeEnd()))
		//							if (s1.getDateRangeStart().getTime() == (se.getDateRangeStart().getTime()))
		//								if (s1.getDateRangeEnd().getTime() == (se.getDateRangeEnd().getTime()))
		//									this.delete(s1);
		//		}
		//		/////////////////////////////////////////////////////////////////////////////////////////
		//		se.setSearchMoment(new Date(System.currentTimeMillis() - 2000));
		//		se.setmillis(LocalDateTime.now().getMillisOfSecond());
		se.setSearchMoment(new Date());

		final Search result = this.searchRepository.save(se);

		final Explorer e = (Explorer) this.actorservice.findActorByPrincipal();
		if (e.getSearches().contains(se))
			e.getSearches().remove(se);
		e.getSearches().add(result);
		this.explorerservice.save(e);

		return result;

	}
	public void delete(final Search search) {

		assert search != null;
		assert search.getId() != 0;

		Explorer e = this.explorerservice.findExplorerWithSearch(search);
		if (e == null)
			e = (Explorer) this.actorservice.findActorByPrincipal();
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

	public Search getSearchFromExplorer(final int explorerId) {
		final Explorer explorer = this.explorerservice.findOne(explorerId);
		Search search = null;
		final Collection<Search> discardedSearches = new ArrayList<Search>();
		final Date d = new Date();
		for (final Search s : explorer.getSearches())
			if (d.getTime() - s.getSearchMoment().getTime() >= (this.configurationService.findConfiguration().getSearchTimeout() * 3600000))
				discardedSearches.add(s);
			else if (search == null)
				search = s;
			else if (s.getSearchMoment().after(search.getSearchMoment()))
				search = s;
			else
				discardedSearches.add(s);

		for (final Search s : discardedSearches)
			this.delete(s);
		if (search == null)
			search = this.create();
		return search;
	}
}
