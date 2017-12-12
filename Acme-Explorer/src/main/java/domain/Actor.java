
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import security.UserAccount;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Actor() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	name;
	private String	surname;
	private String	email;
	private String	address;
	private String	phoneNumber;
	private boolean	isBanned;
	private boolean	suspicious;


	public boolean isSuspicious() {
		return this.suspicious;
	}

	public void setSuspicious(final boolean suspicious) {
		this.suspicious = suspicious;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean getIsBanned() {
		return this.isBanned;
	}

	public void setIsBanned(final boolean isBanned) {
		this.isBanned = isBanned;
	}


	// Relationships ----------------------------------------------------------

	private UserAccount					userAccount;
	private Collection<MessageFolder>	messageFolders;
	private Collection<SocialIdentity>	socialIdentities;


	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@NotNull
	@OneToMany
	public Collection<MessageFolder> getMessageFolders() {
		return this.messageFolders;
	}

	public void setMessageFolders(final Collection<MessageFolder> messageFolder) {
		this.messageFolders = messageFolder;
	}

	@NotNull
	@OneToMany
	public Collection<SocialIdentity> getSocialIdentities() {
		return this.socialIdentities;
	}

	public void setSocialIdentities(final Collection<SocialIdentity> socialIdentities) {
		this.socialIdentities = socialIdentities;
	}

}
