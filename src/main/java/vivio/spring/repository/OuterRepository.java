package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.ClothesOuter;
import vivio.spring.domain.User;

import java.util.List;

@Repository
public interface OuterRepository extends JpaRepository<ClothesOuter, Long> {
    List<ClothesOuter> findAllByUser(User user);

}
