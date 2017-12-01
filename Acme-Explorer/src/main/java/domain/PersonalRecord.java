
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PersonalRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public PersonalRecord() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	nameOfCandidate;
	private String	photo;
	private String	email;
	private String	phoneNumber;
	private String	linkedInProfileURL;


	@NotBlank
	public String getNameOfCandidate() {
		return this.nameOfCandidate;
	}

	public void setNameOfCandidate(final String nameOfCandidate) {
		this.nameOfCandidate = nameOfCandidate;
	}

	@URL
	@NotBlank
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@Email
	@NotBlank
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
	@URL
	@NotBlank
	public String getLinkedInProfileURL() {
		return this.linkedInProfileURL;
	}

	public void setLinkedInProfileURL(final String linkedInProfileURL) {
		this.linkedInProfileURL = linkedInProfileURL;
	}

	// Relationships ----------------------------------------------------------

}
