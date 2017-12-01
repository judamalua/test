
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AuditRecordRepository;
import domain.AuditRecord;

@Component
@Transactional
public class StringToAuditRecordConverter implements Converter<String, AuditRecord> {

	@Autowired
	AuditRecordRepository	auditRecordRepository;


	@Override
	public AuditRecord convert(final String text) {
		AuditRecord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.auditRecordRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
