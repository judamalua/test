
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

	@Query("select ta from Trip t join t.tags ta where t.id = ?1")
	Collection<Tag> findTagsByTrip(int tripId);
}
