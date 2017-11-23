
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Indexed
public class Curriculum extends DomainEntity {

	//Constructors -----------------------------------------------------------
	//	public Curriculum() {
	//		super();
	//		this.professionalRecords = new HashSet<ProfessionalRecord>();
	//	}

	// Attributes -------------------------------------------------------------
	private String	ticker;


	@NotBlank
	@Pattern(regexp = "^[0-9]{6}-[A-Z]{4}$")
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}


	// Relationships ----------------------------------------------------------

	private PersonalRecord					personalRecord;
	private Ranger							ranger;
	private Collection<ProfessionalRecord>	professionalRecords;
	private Collection<EducationRecord>		educationRecords;
	private Collection<EndorserRecord>		endorserRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	public PersonalRecord getPersonalRecord() {
		return this.personalRecord;
	}

	public void setPersonalRecord(final PersonalRecord personalRecord) {
		this.personalRecord = personalRecord;
	}

	@Valid
	@OneToOne(optional = false)
	public Ranger getRanger() {
		return this.ranger;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<ProfessionalRecord> getProfessionalRecords() {
		return this.professionalRecords;
	}

	public void setProfessionalRecords(final Collection<ProfessionalRecord> professionalRecords) {
		this.professionalRecords = professionalRecords;
	}

	public void addProfessionalRecord(final ProfessionalRecord professionalRecord) {
		this.professionalRecords.add(professionalRecord);
	}

	public void removeProfessionalRecord(final ProfessionalRecord professionalRecord) {
		this.professionalRecords.remove(professionalRecord);
	}

	public void setRanger(final Ranger ranger) {
		this.ranger = ranger;
	}

	@NotNull
	@OneToMany(cascade = {
		CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	public Collection<EducationRecord> getEducationRecords() {
		return this.educationRecords;
	}

	public void setEducationRecords(final Collection<EducationRecord> educationRecords) {
		this.educationRecords = educationRecords;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<EndorserRecord> getEndorserRecords() {
		return this.endorserRecords;
	}

	public void setEndorserRecords(final Collection<EndorserRecord> endorserRecords) {
		this.endorserRecords = endorserRecords;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

}
