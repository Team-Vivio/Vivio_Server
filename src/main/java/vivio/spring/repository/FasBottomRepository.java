package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;

import java.util.List;
import java.util.Optional;

@Repository
public interface FasBottomRepository extends JpaRepository<FashionBottom,Long> {
    Optional<FashionBottom> findFirstByFashionRecommandId(Long id);
    List<FashionBottom> findAllByFashionRecommandId(Long id);
}
