
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Search;

@Component
@Transactional
public class SearchToStringConverter implements Converter<Search, String> {

	@Override
	public String convert(final Search search) {
		String result;

		if (search == null)
			result = null;
		else
			result = String.valueOf(search.getId());

		return result;
	}

}
