package vivio.spring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.User;

import java.util.List;

public interface FasCloRepository extends JpaRepository<FashionCloset,Long> {
    List<FashionCloset> findAllByUser(User user, Sort sort);
}
