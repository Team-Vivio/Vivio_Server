package vivio.spring.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.Beauty;
import vivio.spring.domain.Color;
import vivio.spring.domain.ColorRecommend;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ColorRecommendColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="colorRecommend")
    private ColorRecommend colorRecommend;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="color")
    private Color color;
}
