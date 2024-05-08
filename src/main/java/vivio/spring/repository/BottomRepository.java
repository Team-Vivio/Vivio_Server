package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.Bottom;
import vivio.spring.domain.User;

import java.util.List;

@Repository
public interface BottomRepository extends JpaRepository<Bottom, Long> {
    List<Bottom> findAllByUser(User user);

    void deleteByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);

}
