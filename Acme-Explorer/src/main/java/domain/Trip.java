
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Indexed
public class Trip extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	ticker;
	private String	title;
	private String	description;
	private double	price;
	private String	requirements;
	private Date	startDate;
	private Date	endDate;
	private Date	publicationDate;
	private String	cancelReason;


	@Field
	@NotBlank
	@Pattern(regexp = "^[0-9]{6}-[A-Z]{4}$")
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	@Field
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}
	@Field
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

	@NotBlank
	public String getRequirements() {
		return this.requirements;
	}

	public void setRequirements(final String requirements) {
		this.requirements = requirements;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getCancelReason() {
		return this.cancelReason;
	}

	public void setCancelReason(final String cancelReason) {
		this.cancelReason = cancelReason;
	}


	// Relationships ----------------------------------------------------------

	private Collection<Sponsorship>		sponsorships;
	private Collection<Stage>			stages;
	private Collection<Story>			stories;
	private Collection<SurvivalClass>	survivalClasses;
	//	private Collection<Tag>				tags;
	private Ranger						ranger;
	private LegalText					legalText;
	private Category					category;
	private Collection<AuditRecord>		auditRecords;
	private Collection<Note>			notes;
	private Collection<Manager>			managers;
	private Collection<Application>		applications;
	private Collection<TagValue>		tagValues;


	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@NotEmpty
	@ManyToMany(mappedBy = "trips")
	public Collection<Manager> getManagers() {
		return this.managers;
	}

	public void setManagers(final Collection<Manager> managers) {
		this.managers = managers;
	}

	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Note> getNotes() {
		return this.notes;
	}

	public void setNotes(final Collection<Note> notes) {
		this.notes = notes;
	}

	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<AuditRecord> getAuditRecords() {
		return this.auditRecords;
	}

	public void setAuditRecords(final Collection<AuditRecord> auditRecords) {
		this.auditRecords = auditRecords;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	@Valid
	@ManyToOne(optional = true)
	public LegalText getLegalText() {
		return this.legalText;
	}

	public void setLegalText(final LegalText legalText) {
		this.legalText = legalText;
	}

	@NotNull
	@OneToMany(mappedBy = "trip")
	public Collection<Sponsorship> getSponsorships() {
		return this.sponsorships;
	}

	public void setSponsorships(final Collection<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

	public void addSponsorship(final Sponsorship sponsorship) {
		this.sponsorships.add(sponsorship);
		sponsorship.setTrip(this);
	}

	public void removeSponsorship(final Sponsorship sponsorship) {
		this.sponsorships.remove(sponsorship);
		sponsorship.setTrip(null);
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Stage> getStages() {
		return this.stages;
	}

	public void setStages(final Collection<Stage> stages) {
		this.stages = stages;
	}

	@NotNull
	@OneToMany
	public Collection<Story> getStories() {
		return this.stories;
	}

	public void setStories(final Collection<Story> stories) {
		this.stories = stories;
	}

	@NotNull
	@ManyToMany
	public Collection<SurvivalClass> getSurvivalClasses() {
		return this.survivalClasses;
	}

	public void setSurvivalClasses(final Collection<SurvivalClass> survivalClasses) {
		this.survivalClasses = survivalClasses;
	}

	//	@NotNull
	//	@ManyToMany
	//	public Collection<Tag> getTags() {
	//		return this.tags;
	//	}
	//
	//	public void setTags(final Collection<Tag> tags) {
	//		this.tags = tags;
	//	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Ranger getRanger() {
		return this.ranger;
	}

	public void setRanger(final Ranger ranger) {
		this.ranger = ranger;
	}

	@NotNull
	@OneToMany
	public Collection<TagValue> getTagValues() {
		return this.tagValues;
	}

	public void setTagValues(final Collection<TagValue> tagValues) {
		this.tagValues = tagValues;
	}

}
