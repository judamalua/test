
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Tag extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public Tag() {
	//		super();
	//
	//		this.trips = new HashSet<Trip>();
	//	}

	// Attributes -------------------------------------------------------------
	private String	name;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}


	// Relationships ----------------------------------------------------------
	private Collection<TagValue>	tagValues;


	@Valid
	@OneToMany
	public Collection<TagValue> getTagValues() {
		return this.tagValues;
	}

	public void setTagValue(final Collection<TagValue> tagValues) {
		this.tagValues = tagValues;
	}

}
