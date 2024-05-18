package vivio.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.domain.enums.Fashions;

import javax.mail.FetchProfile;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FasCloResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDTO{
        List<CreateItemDTO> items;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItemDTO{
        String outer;
        String top;
        String bottom;
        String fashionName;
        String description;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FasTagItemDTO{
        MultipartFile image;
        ArrayList<Double> probabilities;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FasCloItems{
        Fashions fashion;
        List<MultipartFile> tops;
        List<MultipartFile> bottoms;
        List<MultipartFile> outers;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FasCloJoinDTO{
        Long id;
        LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewListResultDTO{
        List<ViewListItemDTO> items;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewListItemDTO{
        Long id;
        String image;
        String style;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewItemDTO{
        Long id;
        String name;
        List<ItemDTO> items;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDTO{
        Long id;
        String top;
        String bottom;
        String outer;
        String fashionName;
        String description;

    }

}
