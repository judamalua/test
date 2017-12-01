
package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtilityAPlus {

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	public static void muestraFolder(final File fileJson, final Class nameClass) throws JsonMappingException, JsonParseException {

		final ObjectMapper objectMapper = new ObjectMapper();
		List<Object> f = new ArrayList<Object>();
		try {
			f = objectMapper.readValue(fileJson, f.getClass());

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (final Object o : f) {
			final LinkedHashMap<Object, Object> map = (LinkedHashMap<Object, Object>) o;

			System.out.println(objectMapper.convertValue(map, nameClass));
		}
	}

}
