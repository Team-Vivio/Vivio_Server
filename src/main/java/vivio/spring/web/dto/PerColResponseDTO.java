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
        List<ColorDTO> colors;
        String description;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorDTO{
        private String code;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewPersonalColorDTO {
        List<ColorDTO> colors;
        String description;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewBeautyDTO {

        String description;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewAllResultDTO {

        List<ViewListResultDTO> viewListResultDTOS;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewListResultDTO {
        Long PerColId;
        String image;
        Session session;
        Tone tone;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewResponseDTO {
        String image;
        String name;
       Gender gender;
       Session session;
       Tone tone;
       ViewHairDTO hair;
       ViewPersonalColorDTO personalColor;
       ViewBeautyDTO beauty;

    }

}
