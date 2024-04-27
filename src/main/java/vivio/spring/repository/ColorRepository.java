package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.Color;
import vivio.spring.domain.mapping.HairColor;

import java.util.List;
import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {

}
