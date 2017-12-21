
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.TagValueRepository;
import domain.TagValue;

@Component
@Transactional
public class StringToTagValueConverter implements Converter<String, TagValue> {

	@Autowired
	TagValueRepository	tagValueRepository;


	@Override
	public TagValue convert(final String text) {
		TagValue result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.tagValueRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
