
package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Location;

@Component
@Transactional
public class LocationToStringConverter implements Converter<Location, String> {

	@Override
	public String convert(final Location location) {
		String result;
		StringBuilder builder;

		if (location == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(location.getName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(location.getGpsCoordinates(), "UTF-8"));

				result = builder.toString();

			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return result;
	}
}
