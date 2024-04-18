package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.Color;
import vivio.spring.domain.FashionColor;

import java.util.Optional;
@Repository
public interface FasColorRepository extends JpaRepository<FashionColor,Long> {
     Optional<FashionColor> findByName(String color);


}
