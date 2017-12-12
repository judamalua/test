
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Contact;

@Component
@Transactional
public class ContactToStringConverter implements Converter<Contact, String> {

	@Override
	public String convert(final Contact contact) {
		String result;

		if (contact == null)
			result = null;
		else
			result = String.valueOf(contact.getId());

		return result;
	}

}
