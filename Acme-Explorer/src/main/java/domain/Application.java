
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Application extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Application() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private Date		date;
	private String		status;
	private String		commentaries;
	private CreditCard	creditCard;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	@NotBlank
	@Pattern(regexp = "PENDING|REJECTED|DUE|ACCEPTED|CANCELLED")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getCommentaries() {
		return this.commentaries;
	}

	public void setCommentaries(final String commentaries) {
		this.commentaries = commentaries;
	}

	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	// Relationships ----------------------------------------------------------

	private Rejection	rejection;
	private Trip		trip;


	@OneToOne(mappedBy = "application", optional = true)
	@Valid
	public Rejection getRejection() {
		return this.rejection;
	}

	public void setRejection(final Rejection rejection) {
		this.rejection = rejection;
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
