
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import cz.jirutka.validator.collection.constraints.EachNotEmpty;
import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class Story extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Story() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String				title;
	private String				pieceOfText;
	private Collection<String>	attachments;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getPieceOfText() {
		return this.pieceOfText;
	}

	public void setPieceOfText(final String pieceOfText) {
		this.pieceOfText = pieceOfText;
	}

	@ElementCollection
	@EachURL
	@EachNotEmpty
	public Collection<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final Collection<String> attachments) {
		this.attachments = attachments;
	}

	// Relationships ----------------------------------------------------------

}
