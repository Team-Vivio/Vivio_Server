package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.FashionStyle;

import java.util.Optional;

public interface FasStyRepository extends JpaRepository<FashionStyle,Long> {
    Optional<FashionStyle> findByName(String fashionName);
}
