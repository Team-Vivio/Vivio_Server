package vivio.spring.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.FashionStyle;
import vivio.spring.domain.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FashionClosetFashionStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fashionCloset")
    private FashionCloset fashionCloset;
    @Column(name = "`outer`")
    private String outer;
    @Column(name = "`top`")
    private String top;
    private String bottom;
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="fashionStyle")
    private FashionStyle fashionStyle;
}
