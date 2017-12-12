
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class EndorserRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public EndorserRecord() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	fullName;
	private String	email;
	private String	phoneNumber;
	private String	linkedInProfileURL;
	private String	commentaries;


	@NotBlank
	public String getFullName() {
		return this.fullName;
	}
	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@NotBlank
	@URL
	public String getLinkedInProfileURL() {
		return this.linkedInProfileURL;
	}
	public void setLinkedInProfileURL(final String linkedInProfileURL) {
		this.linkedInProfileURL = linkedInProfileURL;
	}

	public String getCommentaries() {
		return this.commentaries;
	}

	public void setCommentaries(final String commentaries) {
		this.commentaries = commentaries;
	}

	// Relationships ----------------------------------------------------------

}
