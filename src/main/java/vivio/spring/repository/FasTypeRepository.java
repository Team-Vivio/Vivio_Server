package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.FashionType;

import java.util.Optional;
@Repository
public interface FasTypeRepository extends JpaRepository<FashionType, Long> {
    Optional<FashionType> findByName(String type);
}
