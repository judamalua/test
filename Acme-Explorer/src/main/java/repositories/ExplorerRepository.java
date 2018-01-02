
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Explorer;

@Repository
public interface ExplorerRepository extends JpaRepository<Explorer, Integer> {

	@Query("select e from Explorer e join e.applications a where a.id = ?1")
	Explorer findExplorerByApplication(int applicationId);

	@Query("select a from Explorer e join e.applications a where e.id = ?1 and a.trip.id= ?2 and a.status = 'ACCEPTED'")
	Application findApplicationByExplorerTrip(int idExplorer, int idtrip);

	@Query("select e from Explorer e where e.search.id=?1")
	Explorer findExplorerBySearch(int id);

}
