
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SurvivalClassRepository;
import domain.SurvivalClass;

@Component
@Transactional
public class StringToSurvivalClassConverter implements Converter<String, SurvivalClass> {

	@Autowired
	SurvivalClassRepository	survivalClassRepository;


	@Override
	public SurvivalClass convert(final String text) {
		SurvivalClass result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.survivalClassRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
