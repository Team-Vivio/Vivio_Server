package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.Beauty;

public interface PerBeautyRepository extends JpaRepository<Beauty,Long> {
}
