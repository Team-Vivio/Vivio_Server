package vivio.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.common.BaseEntity;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FashionCloset extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;


}
