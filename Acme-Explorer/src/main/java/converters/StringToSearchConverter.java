
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SearchRepository;
import domain.Search;

@Component
@Transactional
public class StringToSearchConverter implements Converter<String, Search> {

	@Autowired
	SearchRepository	searchRepository;


	@Override
	public Search convert(final String text) {
		Search result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.searchRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
