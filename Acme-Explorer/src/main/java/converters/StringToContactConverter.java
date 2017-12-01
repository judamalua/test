
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ContactRepository;
import domain.Contact;

@Component
@Transactional
public class StringToContactConverter implements Converter<String, Contact> {

	@Autowired
	ContactRepository	contactRepository;


	@Override
	public Contact convert(final String text) {
		Contact result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.contactRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
