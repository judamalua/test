
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
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


	@NotBlank
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
}
