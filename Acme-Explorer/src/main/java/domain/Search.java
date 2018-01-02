
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Search extends DomainEntity {

	// Constructors -----------------------------------------------------------

	//	public Search() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	keyWord;
	private Double	priceRangeStart;
	private Double	priceRangeEnd;
	private Date	dateRangeStart;
	private Date	dateRangeEnd;
	private Date	searchMoment;


	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	public Double getPriceRangeStart() {
		return this.priceRangeStart;
	}

	public void setPriceRangeStart(final Double priceRangeStart) {
		this.priceRangeStart = priceRangeStart;
	}

	public Double getPriceRangeEnd() {
		return this.priceRangeEnd;
	}

	public void setPriceRangeEnd(final Double priceRangeEnd) {
		this.priceRangeEnd = priceRangeEnd;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDateRangeStart() {
		return this.dateRangeStart;
	}

	public void setDateRangeStart(final Date dateRangeStart) {
		this.dateRangeStart = dateRangeStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDateRangeEnd() {
		return this.dateRangeEnd;
	}

	public void setDateRangeEnd(final Date dateRangeEnd) {
		this.dateRangeEnd = dateRangeEnd;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSearchMoment() {
		return this.searchMoment;
	}

	public void setSearchMoment(final Date searchMoment) {
		this.searchMoment = searchMoment;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Trip>	trips;


	@NotNull
	@ManyToMany
	public Collection<Trip> getTrips() {
		return this.trips;
	}

	public void setTrips(final Collection<Trip> trips) {
		this.trips = trips;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.dateRangeEnd == null) ? 0 : this.dateRangeEnd.hashCode());
		result = prime * result + ((this.dateRangeStart == null) ? 0 : this.dateRangeStart.hashCode());
		result = prime * result + ((this.keyWord == null) ? 0 : this.keyWord.hashCode());
		result = prime * result + ((this.priceRangeEnd == null) ? 0 : this.priceRangeEnd.hashCode());
		result = prime * result + ((this.priceRangeStart == null) ? 0 : this.priceRangeStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Search other = (Search) obj;
		if (this.dateRangeEnd == null) {
			if (other.dateRangeEnd != null)
				return false;
		} else if (!this.dateRangeEnd.equals(other.dateRangeEnd))
			return false;
		if (this.dateRangeStart == null) {
			if (other.dateRangeStart != null)
				return false;
		} else if (!this.dateRangeStart.equals(other.dateRangeStart))
			return false;
		if (this.keyWord == null) {
			if (other.keyWord != null)
				return false;
		} else if (!this.keyWord.equals(other.keyWord))
			return false;
		if (this.priceRangeEnd == null) {
			if (other.priceRangeEnd != null)
				return false;
		} else if (!this.priceRangeEnd.equals(other.priceRangeEnd))
			return false;
		if (this.priceRangeStart == null) {
			if (other.priceRangeStart != null)
				return false;
		} else if (!this.priceRangeStart.equals(other.priceRangeStart))
			return false;
		return true;
	}
}
