
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	@Query("select c.ticker from Curriculum c")
	Collection<String> getCurriculumTickers();

	@Query("select c from Curriculum c  join c.endorserRecords e where e.id=?1")
	Curriculum getCurriculumWithEndorser(int id);

	@Query("select c from Curriculum c  join c.miscellaneousRecords e where e.id=?1")
	Curriculum getCurriculumWithMiscellaneous(int id);

	@Query("select c from Curriculum c  join c.educationRecords e where e.id=?1")
	Curriculum getCurriculumWithEducation(int id);

	@Query("select c from Curriculum c  join c.professionalRecords e where e.id=?1")
	Curriculum getCurriculumWithProfessional(int id);

	@Query("select c from Curriculum c  where c.ranger.id=?1")
	Curriculum getCurriculumByRangerID(int id);

}
