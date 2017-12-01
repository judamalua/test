
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsor extends Actor {

	// Constructors -----------------------------------------------------------
	//	public Sponsor() {
	//		super();
	//		this.sponsorships = new HashSet<Sponsorship>();
	//	}

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Sponsorship>	sponsorships;


	@NotNull
	@OneToMany(mappedBy = "sponsor")
	public Collection<Sponsorship> getSponsorships() {
		return this.sponsorships;
	}

	public void setSponsorships(final Collection<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

	//	public void addSponsorship(final Sponsorship sponsorship) {
	//		this.sponsorships.add(sponsorship);
	//		sponsorship.setSponsor(this);
	//	}
	//
	//	public void removeSponsorship(final Sponsorship sponsorship) {
	//		this.sponsorships.remove(sponsorship);
	//		sponsorship.setSponsor(null);
	//	}

}
