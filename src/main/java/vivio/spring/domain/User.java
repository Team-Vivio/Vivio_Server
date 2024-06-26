package vivio.spring.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vivio.spring.domain.common.BaseEntity;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Platform;
import vivio.spring.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String name;
    @Column(columnDefinition = "VARCHAR(100)")
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Column(columnDefinition = "VARCHAR(200)")
    private String email;
    @Column(columnDefinition = "VARCHAR(20)")
    private String password;
    @Column(columnDefinition = "VARCHAR(50)")
    private String phoneNumber;
    private int coin;
    private LocalDate birthDate;

    private String provider;
    private String providerId;
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
