
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findActorByUserAccountId(Integer id);

	@Query("select a from Actor a join a.messageFolders m where m.id = ?1")
	Actor findActorByMessageFolder(Integer id);

	@Query("select a from Actor a where a.suspicious=true")
	Collection<Actor> findSuspicious();

}
