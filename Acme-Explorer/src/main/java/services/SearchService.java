
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SearchRepository;
import domain.Explorer;
import domain.Search;
import domain.Trip;

@Service
@Transactional
public class SearchService {

	// Managed repository --------------------------------------------------

	@Autowired
	private SearchRepository		searchRepository;

	// Supporting services -------------------------------------------------
	@Autowired
	private ExplorerService			explorerservice;
	@Autowired
	private ActorService			actorservice;
	@Autowired
	private TripService				tripService;
	@Autowired
	private ConfigurationService	configurationService;


	// Simple CRUD methods --------------------------------------------------
	public Search create() {

		Search result;
		Page<Trip> trips;
		Pageable pageable;

		result = new Search();
		pageable = new PageRequest(0, this.configurationService.findConfiguration().getMaxResults());
		trips = this.tripService.findPublicatedTrips(pageable);

		result.setSearchMoment(new Date(System.currentTimeMillis() - 2000));
		result.setTrips(trips.getContent());
		return result;
	}

	public Collection<Search> findAll() {
		//this.checkSearchDataBase();
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

	public Search save(final Search se) {

		Assert.notNull(se);

		Search result;
		final Explorer explorer;

		se.setSearchMoment(new Date());

		result = this.searchRepository.save(se);

		explorer = (Explorer) this.actorservice.findActorByPrincipal();

		explorer.setSearch(result);
		this.explorerservice.save(explorer);

		return result;

	}
	public void delete(final Search search) {

		assert search != null;
		assert search.getId() != 0;

		Explorer e = this.explorerservice.findExplorerWithSearch(search);
		if (e == null)
			e = (Explorer) this.actorservice.findActorByPrincipal();
		Assert.notNull(e);

		this.searchRepository.delete(search);

	}
	// Other Business  methods --------------------------------------------------

	//	public void checkSearchDataBase() {
	//		final Collection<Search> searchs = this.searchRepository.findAll();
	//		long diferencia;
	//		for (final Search s : searchs) {
	//			final Date d = new Date();
	//			diferencia = d.getTime() - s.getSearchMoment().getTime();
	//			if (diferencia >= 3600000)
	//				this.delete(s);
	//
	//		}
	//	}
	//
	//	public Search getSearchFromExplorer(final int explorerId) {
	//		final Explorer explorer = this.explorerservice.findOne(explorerId);
	//		Search search = null;
	//		final Collection<Search> discardedSearches = new ArrayList<Search>();
	//		final Date d = new Date();
	//		for (final Search s : explorer.getSearches())
	//			if (d.getTime() - s.getSearchMoment().getTime() >= (this.configurationService.findConfiguration().getSearchTimeout() * 3600000))
	//				discardedSearches.add(s);
	//			else if (search == null)
	//				search = s;
	//			else if (s.getSearchMoment().after(search.getSearchMoment()))
	//				search = s;
	//			else
	//				discardedSearches.add(s);
	//
	//		for (final Search s : discardedSearches)
	//			this.delete(s);
	//		if (search == null)
	//			search = this.create();
	//		return search;
	//	}
}
