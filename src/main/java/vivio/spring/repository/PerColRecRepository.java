package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.ColorRecommend;

public interface PerColRecRepository extends JpaRepository<ColorRecommend, Long> {
}
