
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.RejectionRepository;
import domain.Rejection;

@Component
@Transactional
public class StringToRejectionConverter implements Converter<String, Rejection> {

	@Autowired
	RejectionRepository	rejectionRepository;


	@Override
	public Rejection convert(final String text) {
		Rejection result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.rejectionRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
