
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.SurvivalClass;

@Component
@Transactional
public class SurvivalClassToStringConverter implements Converter<SurvivalClass, String> {

	@Override
	public String convert(final SurvivalClass survivalClass) {
		String result;

		if (survivalClass == null)
			result = null;
		else
			result = String.valueOf(survivalClass.getId());

		return result;
	}

}
