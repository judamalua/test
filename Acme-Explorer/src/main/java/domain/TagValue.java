
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class TagValue extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	value;


	@NotBlank
	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}


	// Relationships ----------------------------------------------------------

	private Tag	tag;


	@Valid
	@ManyToOne(optional = false)
	public Tag getTag() {
		return this.tag;
	}

	public void setTag(final Tag tag) {
		this.tag = tag;
	}

}
