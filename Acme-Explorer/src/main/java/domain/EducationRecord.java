
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
public class EducationRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public EducationRecord() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	diplomaTitle;
	private Date	studyingPeriodStart;
	private Date	studyingPeriodEnd;
	private String	institution;
	private String	attachment;
	private String	commentaries;


	@NotBlank
	public String getDiplomaTitle() {
		return this.diplomaTitle;
	}

	public void setDiplomaTitle(final String diplomaTitle) {
		this.diplomaTitle = diplomaTitle;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getStudyingPeriodStart() {
		return this.studyingPeriodStart;
	}

	public void setStudyingPeriodStart(final Date studyingPeriodStart) {
		this.studyingPeriodStart = studyingPeriodStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStudyingPeriodEnd() {
		return this.studyingPeriodEnd;
	}

	public void setStudyingPeriodEnd(final Date studyingPeriodEnd) {
		this.studyingPeriodEnd = studyingPeriodEnd;
	}

	@NotBlank
	public String getInstitution() {
		return this.institution;
	}

	public void setInstitution(final String institution) {
		this.institution = institution;
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
