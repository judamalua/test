
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SocialIdentity extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public SocialIdentity() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	nick;
	private String	name;
	private String	profileLink;
	private String	photoUrl;


	@NotBlank
	public String getNick() {
		return this.nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@URL
	@NotBlank
	public String getProfileLink() {
		return this.profileLink;
	}

	public void setProfileLink(final String profileLink) {
		this.profileLink = profileLink;
	}

	@URL
	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(final String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
