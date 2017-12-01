
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Note extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Note() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private Date	moment;
	private String	remark;
	private String	reply;
	private Date	momentOfReply;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	public String getReply() {
		return this.reply;
	}
	public void setReply(final String reply) {
		this.reply = reply;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getMomentOfReply() {
		return this.momentOfReply;
	}
	public void setMomentOfReply(final Date momentOfReply) {
		this.momentOfReply = momentOfReply;
	}


	// Relationships ----------------------------------------------------------

	private Auditor	auditor;
	private Trip	trip;
	private Manager	replierManager;


	@ManyToOne(optional = false)
	@Valid
	public Auditor getAuditor() {
		return this.auditor;
	}
	public void setAuditor(final Auditor auditor) {
		this.auditor = auditor;
	}

	@ManyToOne(optional = false)
	@Valid
	public Trip getTrip() {
		return this.trip;
	}
	public void setTrip(final Trip trip) {
		this.trip = trip;
	}

	@ManyToOne(optional = true)
	@Valid
	public Manager getReplierManager() {
		return this.replierManager;
	}
	public void setReplierManager(final Manager replierManager) {
		this.replierManager = replierManager;
	}

}
