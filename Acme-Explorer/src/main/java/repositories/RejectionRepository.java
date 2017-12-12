
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Rejection;

@Repository
public interface RejectionRepository extends JpaRepository<Rejection, Integer> {

}
