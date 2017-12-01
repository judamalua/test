
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SurvivalClass;

@Repository
public interface SurvivalClassRepository extends JpaRepository<SurvivalClass, Integer> {

	@Query("select s from Manager m join m.survivalClasses s where m.id=?1")
	Collection<SurvivalClass> findSurvivalClassByManagerID(Integer id);

	@Query("select e.survivalClasses from Explorer e where e.id = ?1")
	Collection<SurvivalClass> findSurvivalClassesByExplorerID(int id);

	@Query("select s from Trip t join t.survivalClasses s where t.id = ?1")
	Collection<SurvivalClass> findSurvivalClassesByTripId(int tripId);

}
