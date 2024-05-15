package vivio.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import vivio.spring.domain.enums.Gender;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;
    private String url;
    private int price;
    private String title;
}
