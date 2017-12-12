
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Category() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	name;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}


	// Relationships ----------------------------------------------------------
	private Category				fatherCategory;
	private Collection<Category>	categories;


	//private Collection<Trip>		trips;

	@Valid
	@ManyToOne(optional = true)
	public Category getFatherCategory() {
		return this.fatherCategory;
	}

	public void setFatherCategory(final Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

	@NotNull
	@OneToMany(mappedBy = "fatherCategory")
	public Collection<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(final Collection<Category> categories) {
		this.categories = categories;
	}

	//	@NotNull
	//	@OneToMany(mappedBy = "category")
	//	public Collection<Trip> getTrips() {
	//		return this.trips;
	//	}
	//
	//	public void setTrips(final Collection<Trip> trips) {
	//		this.trips = trips;
	//	}

}
