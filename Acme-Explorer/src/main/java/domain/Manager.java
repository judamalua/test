
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	// Constructors -----------------------------------------------------------
	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------

	private Collection<Rejection>		rejections;
	private Collection<Trip>			trips;
	private Collection<SurvivalClass>	survivalClasses;
	private Collection<Note>			repliedNotes;


	@NotNull
	@OneToMany
	public Collection<Rejection> getRejections() {
		return this.rejections;
	}

	public void setRejections(final Collection<Rejection> rejections) {
		this.rejections = rejections;
	}

	@NotNull
	@ManyToMany
	public Collection<Trip> getTrips() {
		return this.trips;
	}

	public void setTrips(final Collection<Trip> trips) {
		this.trips = trips;
	}

	@NotNull
	@OneToMany
	public Collection<SurvivalClass> getSurvivalClasses() {
		return this.survivalClasses;
	}

	public void setSurvivalClasses(final Collection<SurvivalClass> survivalClasses) {
		this.survivalClasses = survivalClasses;
	}

	@NotNull
	@OneToMany(mappedBy = "replierManager")
	public Collection<Note> getRepliedNotes() {
		return this.repliedNotes;
	}

	public void setRepliedNotes(final Collection<Note> repliedNotes) {
		this.repliedNotes = repliedNotes;
	}

}
