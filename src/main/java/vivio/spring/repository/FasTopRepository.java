package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.User;
import vivio.spring.domain.mapping.FashionTop;

import java.util.List;
import java.util.Optional;

@Repository
public interface FasTopRepository extends JpaRepository<FashionTop,Long> {
    Optional<FashionTop> findFirstByFashionRecommandId(Long id);
    List<FashionTop> findAllByFashionRecommandId(Long id);
}
