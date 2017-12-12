
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RangerRepository;
import domain.Ranger;

@Component
@Transactional
public class StringToRangerConverter implements Converter<String, Ranger> {

	@Autowired
	RangerRepository	rangerRepository;


	@Override
	public Ranger convert(final String text) {
		Ranger result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.rangerRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
