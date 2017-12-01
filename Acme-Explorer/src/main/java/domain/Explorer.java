
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
public class Explorer extends Actor {

	// Constructors -----------------------------------------------------------
	//	public Explorer() {
	//		super();
	//		this.stories = new HashSet<Story>();
	//		this.applications = new HashSet<Application>();
	//		this.contacts = new HashSet<Contact>();
	//		this.searches = new HashSet<Search>();
	//	}

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------

	private Collection<Story>			stories;
	private Collection<Application>		applications;
	private Collection<Contact>			contacts;
	private Collection<Search>			searches;
	private Collection<SurvivalClass>	survivalClasses;


	@NotNull
	@OneToMany
	public Collection<Story> getStories() {
		return this.stories;
	}

	public void setStories(final Collection<Story> stories) {
		this.stories = stories;
	}

	@NotNull
	@OneToMany
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@NotNull
	@ManyToMany
	public Collection<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(final Collection<Contact> contacts) {
		this.contacts = contacts;
	}

	@NotNull
	@OneToMany
	public Collection<Search> getSearches() {
		return this.searches;
	}

	public void setSearches(final Collection<Search> searches) {
		this.searches = searches;
	}

	@NotNull
	@ManyToMany
	public Collection<SurvivalClass> getSurvivalClasses() {
		return this.survivalClasses;
	}

	public void setSurvivalClasses(final Collection<SurvivalClass> survivalClasses) {
		this.survivalClasses = survivalClasses;
	}

}
