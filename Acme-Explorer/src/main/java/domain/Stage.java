
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Stage extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Stage() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	title;
	private String	description;
	private double	price;


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

	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	// Relationships ----------------------------------------------------------

}
