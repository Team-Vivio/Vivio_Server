package vivio.spring.repository;

import io.lettuce.core.GeoArgs;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.PersonalColor;
import vivio.spring.domain.User;

import java.util.List;

public interface PerColRepository extends JpaRepository<PersonalColor,Long> {
    List<PersonalColor> findAllByUser(User user, Sort sort);
}
