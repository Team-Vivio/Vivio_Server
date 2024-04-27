package vivio.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vivio.spring.domain.Color;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Session;
import vivio.spring.domain.enums.Tone;

import java.time.LocalDateTime;
import java.util.List;

public class PerColResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponseDTO {
        Long PerColId;
        LocalDateTime createdAt;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResponseDTO {

       Gender gender;
       Session session;
       Tone tone;
       ViewHairDTO hair;
       ViewPersonalColorDTO personalColor;
       ViewBeautyDTO beauty;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewHairDTO {
        List<Color> colors;
        String description;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewPersonalColorDTO {
        List<Color> colors;
        String description;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewBeautyDTO {

        String description;

    }

}
