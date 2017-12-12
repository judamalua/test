
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Indexed
public class LegalText extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public LegalText() {
	//		super();
	//		this.applicableLaws = new ArrayList<String>();
	//	}

	// Attributes -------------------------------------------------------------

	private String				title;
	private String				body;
	private Date				registrationDate;
	private Collection<String>	applicableLaws;
	private boolean				finalMode;


	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}
	public void setBody(final String body) {
		this.body = body;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getRegistrationDate() {
		return this.registrationDate;
	}
	public void setRegistrationDate(final Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@NotNull
	@EachNotBlank
	@ElementCollection
	public Collection<String> getApplicableLaws() {
		return this.applicableLaws;
	}

	public void setApplicableLaws(final Collection<String> applicableLaws) {
		this.applicableLaws = applicableLaws;
	}

	public void addApplicableLaw(final String applicableLaw) {
		this.applicableLaws.add(applicableLaw);
	}

	public void removeApplicableLaw(final String applicableLaw) {
		this.applicableLaws.remove(applicableLaw);
	}

	/**
	 * @return true if LegalText is saved in final mode, and false if LegalText is saved
	 *         in draft mode.
	 **/
	public boolean getFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

	// Relationships ----------------------------------------------------------

}
