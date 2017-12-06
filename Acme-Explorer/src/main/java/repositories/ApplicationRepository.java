
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select sum(case when(a.status='PENDING') then 1.0 else 0.0 end)/count(a) from Application a")
	String getRatioPendingApplications();

	@Query("select sum(case when(a.status='DUE') then 1.0 else 0.0 end)/count(a) from Application a")
	String getRatioDueApplications();

	@Query("select sum(case when(a.status='ACCEPTED') then 1.0 else 0.0 end)/count(a) from Application a")
	String getRatioAcceptedApplications();

	@Query("select sum(case when(a.status='CANCELLED') then 1.0 else 0.0 end)/count(a) from Application a")
	String getRatioCancelledApplications();

	@Query("select a from Explorer e join e.applications a where e.id=?1 group by a.status ")
	Collection<Application> findApplicationsGroupByStatus(int explorerId);

	@Query("select a from Explorer e join e.applications a where e.id=?1")
	Collection<Application> findApplications(int explorerId);

	@Query("select a from Explorer e join e.applications a where e.id = ?1 and a.trip.id= ?2 and a.status = 'ACCEPTED'")
	Application findApplicationByExplorerTrip(int idExplorer, int idtrip);
}
