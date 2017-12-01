
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ProfessionalRecordRepository;
import domain.ProfessionalRecord;

@Component
@Transactional
public class StringToProfessionalRecordConverter implements Converter<String, ProfessionalRecord> {

	@Autowired
	ProfessionalRecordRepository	professionalRecordRepository;


	@Override
	public ProfessionalRecord convert(final String text) {
		ProfessionalRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.professionalRecordRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
