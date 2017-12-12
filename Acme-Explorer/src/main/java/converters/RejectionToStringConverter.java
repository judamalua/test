
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Rejection;

@Component
@Transactional
public class RejectionToStringConverter implements Converter<Rejection, String> {

	@Override
	public String convert(final Rejection rejection) {
		String result;

		if (rejection == null)
			result = null;
		else
			result = String.valueOf(rejection.getId());

		return result;
	}

}
