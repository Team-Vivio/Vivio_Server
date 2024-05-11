package vivio.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SeaCloResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeaCloListDTO {
        List<SeaCloItemDTO> items;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeaCloItemDTO{
        String title;
        String image;
        String link;
        Integer price;
    }
}
