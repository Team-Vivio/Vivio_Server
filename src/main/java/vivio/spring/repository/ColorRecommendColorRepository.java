package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vivio.spring.domain.ColorRecommend;
import vivio.spring.domain.mapping.ColorRecommendColor;

import java.util.List;

public interface ColorRecommendColorRepository extends JpaRepository<ColorRecommendColor, Long> {
    List<ColorRecommendColor> findAllByColorRecommend(ColorRecommend colorRecommend);
}
