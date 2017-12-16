
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Ranger;

@Repository
public interface RangerRepository extends JpaRepository<Ranger, Integer> {

	@Query(" select sum(case when(r.curriculum is not null) then 1.0 else 0.0 end)/(select count(r1) from Ranger r1) from Ranger r")
	String getRatioRangersWithCurriculum();

	@Query(" select sum(case when(r.curriculum.endorserRecords.size>0) then 1.0 else 0.0 end)/count(r) from Ranger r")
	String getRatioEndorsersRangers();

	@Query(" select sum(case when(r.suspicious=true) then 1.0 else 0.0 end)/count(r) from Ranger r")
	String getRatioSuspiciousRangers();

	@Query("select min(r.trips.size), max(r.trips.size), avg(r.trips.size), sqrt(sum(r.trips.size * r.trips.size) / count(r.trips.size) - (avg(r.trips.size) * avg(r.trips.size))) from Ranger r")
	String getTripsInfoFromRanger();

	@Query("select r from Ranger r where r.suspicious = true")
	Collection<Ranger> findSuspiciousRangers();
}
