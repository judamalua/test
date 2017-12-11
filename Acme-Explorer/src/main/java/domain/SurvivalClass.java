
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class SurvivalClass extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public SurvivalClass() {
	//		super();
	//
	//		this.trips = new HashSet<Trip>();
	//	}

	// Attributes -------------------------------------------------------------
	private String		title;
	private String		description;
	private Date		organisationMoment;
	private Location	location;


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

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getOrganisationMoment() {
		return this.organisationMoment;
	}

	public void setOrganisationMoment(final Date organisationMoment) {
		this.organisationMoment = organisationMoment;
	}

	@NotNull
	@Valid
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(final Location location) {
		this.location = location;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Explorer>	explorers;


	@NotNull
	@ManyToMany(mappedBy = "survivalClasses")
	public Collection<Explorer> getExplorers() {
		return this.explorers;
	}

	public void setExplorers(final Collection<Explorer> explorers) {
		this.explorers = explorers;
	}

}
