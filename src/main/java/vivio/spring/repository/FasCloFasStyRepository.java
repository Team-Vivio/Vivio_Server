package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.mapping.FashionClosetFashionStyle;

import java.util.List;
import java.util.Optional;

public interface FasCloFasStyRepository extends JpaRepository<FashionClosetFashionStyle,Long> {
    Optional<FashionClosetFashionStyle> findByFashionCloset(FashionCloset fashionCloset);

    Optional<FashionClosetFashionStyle> findFirstByFashionCloset(FashionCloset fashionCloset);

    List<FashionClosetFashionStyle> findAllByFashionCloset(FashionCloset fashionCloset);
}
