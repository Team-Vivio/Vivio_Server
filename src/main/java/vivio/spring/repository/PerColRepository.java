package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.PersonalColor;
import vivio.spring.domain.User;

import java.util.List;

public interface PerColRepository extends JpaRepository<PersonalColor,Long> {
    List<PersonalColor> findAllByUser(User user);
}
