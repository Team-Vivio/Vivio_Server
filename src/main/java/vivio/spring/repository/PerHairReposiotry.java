package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.Hair;

public interface PerHairReposiotry extends JpaRepository<Hair, Long> {
}
