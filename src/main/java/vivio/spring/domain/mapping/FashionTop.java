package vivio.spring.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.FashionColor;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.FashionType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FashionTop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fashionRecommand")
    private FashionRecommand fashionRecommand;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="color")
    private FashionColor color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="type")
    private FashionType type;

    private String content;
}
