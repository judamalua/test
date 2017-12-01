
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public Sponsorship() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String		bannerUrl;
	private String		additionalInfoLink;
	private CreditCard	creditCard;


	@NotBlank
	@URL
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	@URL
	public String getAdditionalInfoLink() {
		return this.additionalInfoLink;
	}

	public void setAdditionalInfoLink(final String additionalInfoLink) {
		this.additionalInfoLink = additionalInfoLink;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	// Relationships ----------------------------------------------------------

	private Sponsor	sponsor;
	private Trip	trip;


	@ManyToOne(optional = false)
	@Valid
	public Sponsor getSponsor() {
		return this.sponsor;
	}

	public void setSponsor(final Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	@ManyToOne(optional = false)
	@Valid
	public Trip getTrip() {
		return this.trip;
	}

	public void setTrip(final Trip trip) {
		this.trip = trip;
	}

}
