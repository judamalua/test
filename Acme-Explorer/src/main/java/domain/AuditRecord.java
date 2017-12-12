
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class AuditRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Audit() {
	//		super();
	//		this.attachments = new HashSet<String>();
	//	}

	// Attributes -------------------------------------------------------------
	private Date				momentWhenCarriedOut;
	private String				title;
	private String				description;
	private boolean				isFinalMode;
	private Collection<String>	attachments;


	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMomentWhenCarriedOut() {
		return this.momentWhenCarriedOut;
	}

	public void setMomentWhenCarriedOut(final Date momentWhenCarriedOut) {
		this.momentWhenCarriedOut = momentWhenCarriedOut;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean getIsFinalMode() {
		return this.isFinalMode;
	}

	public void setIsFinalMode(final boolean isFinalMode) {
		this.isFinalMode = isFinalMode;
	}

	@ElementCollection
	@EachNotBlank
	@EachURL
	@NotEmpty
	public Collection<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final Collection<String> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(final String attachment) {
		this.attachments.add(attachment);
	}

	public void removeAttachment(final String attachment) {
		this.attachments.remove(attachment);
	}


	// Relationships ----------------------------------------------------------

	private Auditor	auditor;
	private Trip	trip;


	@Valid
	@ManyToOne(optional = false)
	public Trip getTrip() {
		return this.trip;
	}

	public void setTrip(final Trip trip) {
		this.trip = trip;
	}

	@Valid
	@ManyToOne(optional = false)
	public Auditor getAuditor() {
		return this.auditor;
	}
	public void setAuditor(final Auditor auditor) {
		this.auditor = auditor;
	}
}
