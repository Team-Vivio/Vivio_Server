package vivio.spring.web.dto;

import lombok.Getter;
import lombok.Setter;
import vivio.spring.domain.User;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Type;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;

import java.util.List;

public class FasRecRequestDTO {
    @Getter
    @Setter
    public static class JoinDTO{

        private int gender;
        private float height;
        private float weight;
        private int type;

        private List<FasTopJoinDTO> fashionTops;
        private List<FasBottomJoinDTO> fashionBottoms;


    }
    @Getter
    @Setter
    public static class FasTopJoinDTO{
        private String content;
        private String color;
        private String type;
        private String image;
        private String link;
    }
    @Getter
    @Setter
    public static class FasBottomJoinDTO{
        private String content;
        private String color;
        private String type;
        private String image;
        private String link;
    }
    @Getter
    @Setter
    public static class FasRecCreateDTO{
        private int gender;
        private float height;
        private float weight;
        private int type;
    }

}
