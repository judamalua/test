
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

	@Query("select t from Trip t where t.ticker like ?1 or t.title like ?1 or t.description like ?1 and t.publicationDate < NOW()")
	Collection<Trip> findTrips(String keyword);

	@Query("select t from Trip t where t.category.id = ?1 and t.publicationDate < NOW()")
	Collection<Trip> findTripsByCategoryId(int id);

	@Query("select t from Trip t where t.category.id = ?1")
	Collection<Trip> findAllTripsByCategoryId(int id);

	@Query("select min(t.notes.size), max(t.notes.size), avg(t.notes.size), sqrt(sum(t.notes.size * t.notes.size) / count(t.notes.size) - (avg(t.notes.size) * avg(t.notes.size))) from Trip t")
	String getInfoNotesFromTrips();

	@Query("select min(t.auditRecords.size), max(t.auditRecords.size), avg(t.auditRecords.size), sqrt(sum(t.auditRecords.size * t.auditRecords.size) / count(t.auditRecords.size) - (avg(t.auditRecords.size) * avg(t.auditRecords.size))) from Trip t")
	String getInfoAuditsFromTrips();

	@Query("select sum(case when(t.auditRecords.size>0) then 1.0 else 0.0 end)/count(t) from Trip t")
	String getRatioAuditsFromTrips();

	@Query("select min(t.applications.size), max(t.applications.size), avg(t.applications.size), sqrt(sum(t.applications.size * t.applications.size) / count(t.applications.size) - (avg(t.applications.size) * avg(t.applications.size))) from Trip t")
	String getApplicationsInfoFromTrips();

	@Query("select min(t.price), max(t.price), avg(t.price), sqrt(sum(t.price * t.price) / count(t.price) - (avg(t.price) * avg(t.price))) from Trip t")
	String getPriceInfoFromTrips();

	@Query("select sum(case when(t.cancelReason is not null) then 1.0 else 0.0 end)/count(t)  from Trip t")
	String getRatioTripsCancelled();

	@Query("select t from Trip t where t.applications.size/(select avg(t1.applications.size) from Trip t1)>=1.1 order by t.applications.size")
	Collection<Trip> getTripsTenPercentMoreApplications();

	@Query("select t.legalText.title,(select count(t) from LegalText lt where t.legalText=lt) from Trip t group by t.legalText")
	Collection<String> getNumberOfReferencesLegalTexts();
	//requirement 34
	@Query("select t from Trip t where (t.title like ?1 or t.ticker like ?1 or t.description like ?1)  and (t.startDate between  ?2 and ?3) and (t.price between  ?4 and ?5) and t.publicationDate < NOW() ")
	Page<Trip> findTripsBySearchParameters(String q, Date date1, Date date2, Double pricelow, Double priceHigh, Pageable pageable);

	@Query("select t from Trip t where  t.startDate between  ?1 " + "and ?2 and t.price between  ?3 and ?4 and t.publicationDate <= NOW() ")
	Page<Trip> findTripsBySearchParametersWithoutQ(Date date1, Date date2, Double pricelow, Double priceHigh, Pageable pageable);

	//	@Query("select t from Trip t where (select count(a) from Trip t join t.applications a where a.explorer.id = ?1)>0  and t.publicationDate < CURRENT_DATE")
	//	Collection<Trip> findTripsApplicationExplorer(int explorerID);

	@Query("select t.ticker from Trip t")
	Collection<String> getTripTickers();

	@Query("select t from Trip t join t.stages s where s.id = ?1")
	Trip getTripFromStageId(int stageId);

	@Query("select sum(s.price) from Trip t join t.stages s where t.id=?1")
	double getPriceStages(int tripId);

	@Query("select t from Trip t join t.stories s where s.id=?1")
	Trip getTripFromStory(int storyId);

	@Query("select t from Explorer e join e.applications a join a.trip t where e.id=?1 and a.status='ACCEPTED'")
	Collection<Trip> getAcceptedTripsFromExplorerId(int explorerId);

	//Requirement 44
	@Query("select t from Explorer e join e.applications a join a.trip t join t.survivalClasses s where e.id = ?1 and a.status = 'ACCEPTED' and s.id = ?2 and t.endDate > NOW()")
	Trip findTripSurvivalApplication(int idExplorer, int idSurvivalClass);

	//	//new changes 
	//	@Query("select t from Trip t where t.title like  '?1'")
	//	Collection<Trip> findTripsByTitle(String q);

	@Query("select t from Trip t where t.startDate between '?1' and '?2'")
	Collection<Trip> findTripsByStartDate(Date date1, Date date2);

	@Query("select t from Trip t where t.price between ?1 and ?2")
	Collection<Trip> findTripsByPriceRange(double d1, double d2);

	@Query("select t from Trip t where t.publicationDate <= NOW()")
	Collection<Trip> findTripsByPublicationDate();

	@Query("select t from Trip t where t.title like ?1 and t.price between ?2 and ?3 and t.publicationDate <= NOW() ")
	Collection<Trip> findTripsByTitleAndPricePublication(String q, double d1, double d2);

	@Query("select t from Trip t join t.survivalClasses s where s.id = ?1")
	Collection<Trip> findTripBySurvivalClass(int idSurvivalClass);

	// Paginated Queries

	@Query("select t from Trip t where (t.ticker like ?1 or t.title like ?1 or t.description like ?1) and t.publicationDate <= NOW() and (t.cancelReason like '' or t.cancelReason=null)")
	Page<Trip> findTrips(String keyword, Pageable pageable);

	@Query("select t from Trip t where t.category.id = ?1 and t.publicationDate <= NOW()")
	Page<Trip> findTripsByCategoryId(int id, Pageable pageable);

	//	@Query("select t from Trip t where (select count(a) from Trip t join t.applications a where a.explorer.id = ?1)>0  and t.publicationDate < CURRENT_DATE")
	//	Collection<Trip> findTripsApplicationExplorer(int explorerID);

	@Query("select t from Trip t where t.publicationDate <= NOW() and t.stages.size > 0 and (t.cancelReason like '' or t.cancelReason=null)")
	Page<Trip> findPublicatedTrips(Pageable pageable);

	@Query("select t from Trip t where t.startDate between '?1' and '?2'")
	Page<Trip> findTripsByStartDate(Date date1, Date date2, Pageable pageable);

	@Query("select t from Trip t where t.price between ?1 and ?2")
	Page<Trip> findTripsByPriceRange(double d1, double d2, Pageable pageable);

	@Query("select t from Trip t where t.publicationDate <= NOW()")
	Page<Trip> findTripsByPublicationDate(Pageable pageable);

	@Query("select t from Trip t where t.title like ?1 and t.price between ?2 and ?3 and t.publicationDate <= NOW() ")
	Page<Trip> findTripsByTitleAndPricePublication(String q, double d1, double d2, Pageable pageable);

	@Query("select t from Trip t join t.tagValues tv where tv.tag.id = ?1")
	Collection<Trip> findTripsByTagId(int tagId);

	@Query("select t from Trip t join t.tagValues tv where tv.id = ?1")
	Trip findTripByTagValue(int tagValueId);

}
