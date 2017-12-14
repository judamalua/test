
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Search;
import domain.Trip;

@Service
@Transactional
public class CacheService {

	// Memory Cache ---------------------------------------------------------------------------
	private final Map<Search, Page<Trip>>	caché	= new HashMap<Search, Page<Trip>>();


	// Supporting services --------------------------------------------------------------------------

	// Other business methods --------------------------------------------------------------------------
	public Page<Trip> findInCache(final Search search) {
		Page<Trip> trips = null;
		Search sfinal = null;

		if (this.caché.containsKey(search)) {
			final Set<Search> s = new HashSet<Search>(this.caché.keySet());
			for (final Search s1 : s)
				if (s1.getKeyWord().equals(search.getKeyWord()) && s1.getPriceRangeStart().equals(search.getPriceRangeStart()) && s1.getPriceRangeEnd().equals(search.getPriceRangeEnd()) && s1.getDateRangeStart().equals(search.getDateRangeStart())
					&& s1.getDateRangeEnd().equals(search.getDateRangeEnd()))
					sfinal = s1;
			if (sfinal != null)
				if (sfinal.getKeyWord().equals(search.getKeyWord()) && sfinal.getPriceRangeStart().equals(search.getPriceRangeStart()) && sfinal.getPriceRangeEnd().equals(search.getPriceRangeEnd())
					&& sfinal.getDateRangeStart().equals(search.getDateRangeStart()) && sfinal.getDateRangeEnd().equals(search.getDateRangeEnd()))
					trips = this.caché.get(search);
		}

		return trips;

	}
	public void checkCache() {
		final Collection<Search> keys = this.caché.keySet();
		for (final Search s : keys) {
			final long diferencia = LocalDateTime.now().getMillisOfSecond() - s.getSearchMoment().getTime();
			if (diferencia > 3600000)
				this.caché.remove(s);
		}

	}
	public void saveInCache(final Search s, final Page<Trip> trips) {

		this.caché.put(s, trips);
	}

}
