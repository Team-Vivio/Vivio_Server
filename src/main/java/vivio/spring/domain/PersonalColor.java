package vivio.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.common.BaseEntity;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Session;
import vivio.spring.domain.enums.Tone;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PersonalColor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String image;
    @Enumerated(EnumType.STRING)
    private Session session;
    @Enumerated(EnumType.STRING)
    private Tone tone;

}
