
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.TripRepository;
import domain.Trip;

@Component
@Transactional
public class StringToTripConverter implements Converter<String, Trip> {

	@Autowired
	TripRepository	tripRepository;


	@Override
	public Trip convert(final String text) {
		Trip result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.tripRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
