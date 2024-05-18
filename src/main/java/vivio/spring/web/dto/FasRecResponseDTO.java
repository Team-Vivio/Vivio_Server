package vivio.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Type;

import java.time.LocalDateTime;
import java.util.List;

public class FasRecResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO{
        Long fasRecId;
        LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewListResultDTO{
        Long fasRecId;
        String image;
        String link;
        String type;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewAllResultDTO{
        List<ViewListResultDTO> viewListResultDTOS;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewResultDTO{
        Long fasRecId;
        String name;
        List<ViewFashionTopDTO> fashionTops;
        List<ViewFashionBottomDTO> fashionBottoms;

    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewFashionTopDTO{
        String image;
        String link;
        String color;
        String type;
        String content;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewFashionBottomDTO{
        String image;
        String link;
        String color;
        String type;
        String content;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResultDTO{
        Gender gender;
        Type type;
        float height;
        float weight;
        List<ViewFashionTopDTO> fashionTopDTOS;
        List<ViewFashionBottomDTO> fashionBottomDTOS;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverItemDTO{
        private String title;
        private String link;
        private String image;
        private int price;
    }

}
