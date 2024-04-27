package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.Hair;
import vivio.spring.domain.mapping.HairColor;

import java.util.List;
import java.util.Optional;

public interface HairColorRepository extends JpaRepository<HairColor, Long> {
    List<HairColor> findAllByHair(Hair hair);
}
