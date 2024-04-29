package vivio.spring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FasRecRepository extends JpaRepository<FashionRecommand,Long> {
    List<FashionRecommand> findAllByUser(User user, Sort sort);
}
