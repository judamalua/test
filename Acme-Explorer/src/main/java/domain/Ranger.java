
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Ranger extends Actor {

	// Constructors -----------------------------------------------------------

	//	public Ranger() {
	//		super();
	//		this.trips = new HashSet<Trip>();
	//
	//	}

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Curriculum			curriculum;
	private Collection<Trip>	trips;


	@Valid
	@OneToOne(mappedBy = "ranger", optional = true)
	public Curriculum getCurriculum() {
		return this.curriculum;
	}

	public void setCurriculum(final Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	@NotNull
	@OneToMany(mappedBy = "ranger")
	public Collection<Trip> getTrips() {
		return this.trips;
	}

	public void setTrips(final Collection<Trip> trips) {
		this.trips = trips;
	}

	public void addTrip(final Trip trip) {
		this.trips.add(trip);
		trip.setRanger(this);
	}

	public void removeTrip(final Trip trip) {
		this.trips.remove(trip);
		trip.setRanger(null);
	}

}
