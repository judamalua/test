
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
@Access(AccessType.PROPERTY)
public class Location {

	// Constructors -----------------------------------------------------------
	//	public Location() {
	//
	//	}

	// Attributes -------------------------------------------------------------
	private String	name;
	private String	gpsCoordinates;


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@Pattern(regexp = "^(\\-?\\d+(\\.\\d+)?),\\w*(\\-?\\d+(\\.\\d+)?)$")
	@NotBlank
	public String getGpsCoordinates() {
		return this.gpsCoordinates;
	}
	public void setGpsCoordinates(final String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

}
