
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.AuditRecord;

@Repository
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Integer> {

	@Query("select a from AuditRecord a where a.auditor.id = ?1")
	Collection<AuditRecord> findAuditRecordsByAuditorID(int id);

	@Query("select a from AuditRecord a join a.trip.managers m where m.id=?1")
	Collection<AuditRecord> findAuditRecordsByManagerId(int id);

	@Query("select a from AuditRecord a join a.trip.managers m where m.id=?1 and a.isFinalMode=true")
	Collection<AuditRecord> findAuditRecordsByManagerIdWithNoFinalMode(int id);

}
