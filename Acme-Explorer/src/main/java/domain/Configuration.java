
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Configuration() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private double				vat;
	private int					searchTimeout;
	private Collection<String>	spamWords;
	private String				bannerUrl;
	private String				welcomeMessage;
	private String				defaultPhoneCountryCode;
	private int					maxResults;


	@Min(value = 0)
	public double getVat() {
		return this.vat;
	}

	public void setVat(final double vat) {
		this.vat = vat;
	}

	@Range(min = 1, max = 24)
	public int getSearchTimeout() {
		return this.searchTimeout;
	}

	public void setSearchTimeout(final int searchTimeout) {
		this.searchTimeout = searchTimeout;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@URL
	@NotBlank
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	public String getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@NotBlank
	public String getDefaultPhoneCountryCode() {
		return this.defaultPhoneCountryCode;
	}

	public void setDefaultPhoneCountryCode(final String defaultPhoneCountryCode) {
		this.defaultPhoneCountryCode = defaultPhoneCountryCode;
	}

	@Max(100)
	public int getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(final int maxResults) {
		this.maxResults = maxResults;
	}

}
