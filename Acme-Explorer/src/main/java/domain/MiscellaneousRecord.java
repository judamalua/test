
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MiscellaneousRecord extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public MiscellaneousRecord() {
	//		super();
	//	}

	// Attributes ------------------------------------------------------------------
	private String	title;
	private String	attachment;
	private String	commentaries;


	@Override
	public String toString() {
		return "MiscellaneousRecord [title=" + this.title + ", attachment=" + this.attachment + ", commentaries=" + this.commentaries + "]";
	}
	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getCommentaries() {
		return this.commentaries;
	}

	public void setCommentaries(final String commentaries) {
		this.commentaries = commentaries;
	}

	public String getAttachment() {
		return this.attachment;
	}
	public void setAttachment(final String attachment) {
		this.attachment = attachment;
	}

}
