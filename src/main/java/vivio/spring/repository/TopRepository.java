package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.Top;
import vivio.spring.domain.User;

import java.util.List;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {

    List<Top> findAllByUser(User user);
}
