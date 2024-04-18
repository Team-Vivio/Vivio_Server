package vivio.spring.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.*;
import vivio.spring.domain.enums.Type;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FashionBottom {
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
    private String image;
    private String link;
}
