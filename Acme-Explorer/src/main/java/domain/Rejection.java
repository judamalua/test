
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Rejection extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public Rejection() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	reason;


	@NotBlank
	public String getReason() {
		return this.reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}


	// Relationships ----------------------------------------------------------
	private Application	application;


	@Valid
	@OneToOne(optional = false)
	public Application getApplication() {
		return this.application;
	}

	public void setApplication(final Application application) {
		this.application = application;
	}

}
