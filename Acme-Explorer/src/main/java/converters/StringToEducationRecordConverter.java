
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.EducationRecordRepository;
import domain.EducationRecord;

@Component
@Transactional
public class StringToEducationRecordConverter implements Converter<String, EducationRecord> {

	@Autowired
	EducationRecordRepository	educationRepository;


	@Override
	public EducationRecord convert(final String text) {
		EducationRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.educationRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
