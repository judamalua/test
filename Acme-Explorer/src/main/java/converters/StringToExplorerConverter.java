
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ExplorerRepository;
import domain.Explorer;

@Component
@Transactional
public class StringToExplorerConverter implements Converter<String, Explorer> {

	@Autowired
	ExplorerRepository	explorerRepository;


	@Override
	public Explorer convert(final String text) {
		Explorer result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.explorerRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
