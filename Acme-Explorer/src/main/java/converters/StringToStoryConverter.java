
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.StoryRepository;
import domain.Story;

@Component
@Transactional
public class StringToStoryConverter implements Converter<String, Story> {

	@Autowired
	StoryRepository	storyRepository;


	@Override
	public Story convert(final String text) {
		Story result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.storyRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
