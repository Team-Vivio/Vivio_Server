package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.mapping.BeautyColor;

public interface BeautyColorRepository extends JpaRepository<BeautyColor,Long> {
}
