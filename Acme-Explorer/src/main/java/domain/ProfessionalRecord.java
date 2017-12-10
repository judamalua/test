
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ProfessionalRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public ProfessionalRecord() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	companyName;
	private Date	workingPeriodStart;
	private Date	workingPeriodEnd;
	private String	role;
	private String	attachment;
	private String	commentaries;


	@NotBlank
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getWorkingPeriodStart() {
		return this.workingPeriodStart;
	}

	public void setWorkingPeriodStart(final Date workingPeriodStart) {
		this.workingPeriodStart = workingPeriodStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getWorkingPeriodEnd() {
		return this.workingPeriodEnd;
	}

	public void setWorkingPeriodEnd(final Date workingPeriodEnd) {
		this.workingPeriodEnd = workingPeriodEnd;
	}

	@NotBlank
	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	@URL
	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(final String attachment) {
		this.attachment = attachment;
	}

	public String getCommentaries() {
		return this.commentaries;
	}

	public void setCommentaries(final String commentaries) {
		this.commentaries = commentaries;
	}

	// Relationships ----------------------------------------------------------

}
