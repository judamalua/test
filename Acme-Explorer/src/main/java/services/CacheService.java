
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import domain.Search;
import domain.Trip;

@Service
public class CacheService {

	// Memory Cache ---------------------------------------------------------------------------
	private final Map<Search, Page<Trip>>	cach�	= new HashMap<Search, Page<Trip>>();

	// Supporting services --------------------------------------------------------------------------

	@Autowired
	private SearchService					searchService;


	// Other business methods --------------------------------------------------------------------------
	public Page<Trip> findInCache(final Search search) {
		Page<Trip> trips = null;
		Search sfinal = null;

		if (this.cach�.containsKey(search)) {
			final Set<Search> s = new HashSet<Search>(this.cach�.keySet());
			for (final Search s1 : s)
				if (s1.getKeyWord().equals(search.getKeyWord()) && s1.getPriceRangeStart().equals(search.getPriceRangeStart()) && s1.getPriceRangeEnd().equals(search.getPriceRangeEnd()) && s1.getDateRangeStart().equals(search.getDateRangeStart())
					&& s1.getDateRangeEnd().equals(search.getDateRangeEnd()))
					sfinal = s1;
			if (sfinal != null) {
				this.searchService.save(sfinal);
				trips = this.cach�.get(sfinal);
			}
		}

		return trips;

	}
	public void checkCache() {
		final Collection<Search> keys = this.cach�.keySet();
		for (final Search s : keys) {
			final long diferencia = LocalDateTime.now().getMillisOfSecond() - s.getSearchMoment().getTime();
			if (diferencia > 3600000)
				this.cach�.remove(s);
		}

	}
	public void saveInCache(final Search se, final Page<Trip> trips) {
		final Set<Search> s = new HashSet<Search>(this.cach�.keySet());
		for (final Search s1 : s)
			if (this.cach�.containsKey(s1))
				if (s1.getKeyWord().equals(se.getKeyWord()) && s1.getPriceRangeStart().equals(se.getPriceRangeStart()) && s1.getPriceRangeEnd().equals(se.getPriceRangeEnd()) && s1.getDateRangeStart().equals(se.getDateRangeStart())
					&& s1.getDateRangeEnd().equals(se.getDateRangeEnd()))
					this.cach�.remove(s1);

		this.cach�.put(se, trips);

	}

}
