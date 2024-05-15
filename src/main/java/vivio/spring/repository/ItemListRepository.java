package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.ItemList;

import java.util.Optional;

public interface ItemListRepository extends JpaRepository<ItemList,Long> {
    Optional<ItemList> findByTitle(String itemId);
}
