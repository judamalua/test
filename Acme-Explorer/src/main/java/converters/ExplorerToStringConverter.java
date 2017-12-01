
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Explorer;

@Component
@Transactional
public class ExplorerToStringConverter implements Converter<Explorer, String> {

	@Override
	public String convert(final Explorer explorer) {
		String result;

		if (explorer == null)
			result = null;
		else
			result = String.valueOf(explorer.getId());

		return result;
	}

}
