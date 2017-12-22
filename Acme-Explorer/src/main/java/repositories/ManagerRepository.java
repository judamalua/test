
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Manager;
import domain.Trip;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select sum(case when(m.suspicious=true) then 1.0 else 0.0 end)/count(m) from Manager m")
	String getRatioSuspiciousManagers();

	@Query("select min(m.trips.size), max(m.trips.size), avg(m.trips.size), sqrt(sum(m.trips.size * m.trips.size) / count(m.trips.size) - (avg(m.trips.size) * avg(m.trips.size))) from Manager m")
	String getTripsInfoFromManager();

	@Query("select a from Manager m join m.trips t join t.applications a where m.id=?1")
	Collection<Application> findApplicationsManagedByManager(int managerId);

	@Query("select m from Manager m join m.trips t join t.applications a where a.id=?1")
	Collection<Manager> findManagersManage(int applicationId);

	@Query("select m from Manager m where m.suspicious = true")
	Collection<Manager> findSuspiciousManagers();
	@Query("select m from Manager m join m.rejections r where r.id=?1 ")
	Manager findManagerByRejection(int rejectionId);

	@Query("select m from Manager m join m.survivalClasses s where s.id=?1 ")
	Manager findManagerBySurvivalClass(int survivalClassId);

	@Query("select m from Manager m join m.trips t where t.id=?1 ")
	Collection<Manager> findManagerByTrip(int tripId);

	@Query("select t from Manager m join m.trips t where m.id=?1 ")
	Collection<Trip> findTripsByManager(int managerId);

	@Query("select t from Manager m join m.trips t where m.id=?1 ")
	Page<Trip> findTripsByManager(int managerId, Pageable pageable);
}
