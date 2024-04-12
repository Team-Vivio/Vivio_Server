package vivio.spring.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.Beauty;
import vivio.spring.domain.Color;

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
    @JoinColumn(name="beauty")
    private Beauty beauty;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="color")
    private Color color;
}
