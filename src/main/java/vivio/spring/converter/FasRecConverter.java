package vivio.spring.converter;

import org.json.simple.JSONObject;
import vivio.spring.domain.FashionColor;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.FashionType;
import vivio.spring.domain.User;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Platform;
import vivio.spring.domain.enums.Type;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.web.dto.FasRecRequestDTO;
import vivio.spring.web.dto.FasRecResponseDTO;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

public class FasRecConverter {
    public static FashionRecommand toFashionRecommand(FasRecRequestDTO.JoinDTO request) {
        Gender gender = null;
        Type type = null;
        switch (request.getGender()) {
            case 1:
                gender = Gender.male;
                break;
            case 2:
                gender = Gender.female;
        }
        switch (request.getType()) {
            case 1:
                type = Type.slim;
                break;
            case 2:
                type = Type.normal;
                break;
            case 3:
                type = Type.fat;
                break;
        }

        return FashionRecommand.builder()
                .gender(gender)
                .height(request.getHeight())
                .weight(request.getWeight())
                .type(type)

                .build();
    }
    public static FashionTop toFashionTop(FashionRecommand fashionRecommand, FasRecRequestDTO.FasTopJoinDTO fashionTop, FashionColor color, FashionType type){
        return FashionTop.builder()
                .content(fashionTop.getContent())
                .fashionRecommand(fashionRecommand)
                .link(fashionTop.getLink())
                .image(fashionTop.getImage())
                .color(color)
                .type(type)
                .build();
    }
    public static FashionBottom toFashionBottom(FashionRecommand fashionRecommand, FasRecRequestDTO.FasBottomJoinDTO fashionBottom, FashionColor color, FashionType type){
        return FashionBottom.builder()
                .content(fashionBottom.getContent())
                .fashionRecommand(fashionRecommand)
                .link(fashionBottom.getLink())
                .image(fashionBottom.getImage())
                .color(color)
                .type(type)
                .build();
    }
    public static FasRecResponseDTO.JoinResultDTO toJoinResultDTO(FashionRecommand fashionRecommand) {
        return FasRecResponseDTO.JoinResultDTO.builder()
                .fasRecId(fashionRecommand.getId())
                .name(fashionRecommand.getUser().getName())
                .createdAt(fashionRecommand.getCreatedAt())
                .build();
    }
    public static FasRecResponseDTO.ViewListResultDTO toViewListResultDTO(FashionRecommand fashionRecommand,String image, String link, String type){
        return FasRecResponseDTO.ViewListResultDTO.builder()
                .fasRecId(fashionRecommand.getId())
                .image(image)
                .link(link)
                .type(type)
                .build();
    }
    public static FasRecResponseDTO.ViewAllResultDTO toViewAllResultDTO(List<FasRecResponseDTO.ViewListResultDTO> fashions){
        return FasRecResponseDTO.ViewAllResultDTO.builder()
                .viewListResultDTOS(fashions)
                .build();
    }
    public static FasRecResponseDTO.ViewFashionTopDTO toViewFashionTopDTO(FashionTop fashionTop, String color, String type){
        return FasRecResponseDTO.ViewFashionTopDTO.builder()
                .color(color)
                .type(type)
                .content(fashionTop.getContent())
                .image(fashionTop.getImage())
                .link(fashionTop.getLink())
                .build();
    }
    public static FasRecResponseDTO.ViewFashionBottomDTO toViewFashionBottomDTO(FashionBottom fashionBottom, String color, String type){
        return FasRecResponseDTO.ViewFashionBottomDTO.builder()
                .color(color)
                .type(type)
                .content(fashionBottom.getContent())
                .image(fashionBottom.getImage())
                .link(fashionBottom.getLink())
                .build();
    }
    public static FasRecResponseDTO.ViewResultDTO toViewResultDTO(FashionRecommand fashionRecommand, List<FasRecResponseDTO.ViewFashionBottomDTO> fashionBottomDTOS,List<FasRecResponseDTO.ViewFashionTopDTO> fashionTopDTOS) {
        return FasRecResponseDTO.ViewResultDTO.builder()
                .fasRecId(fashionRecommand.getId())
                .name(fashionRecommand.getUser().getName())
                .fashionTops(fashionTopDTOS)
                .fashionBottoms(fashionBottomDTOS)

                .build();
    }
    public static FasRecResponseDTO.NaverItemDTO toItem(JSONObject item){
        return FasRecResponseDTO.NaverItemDTO.builder()
                .title(item.get("title").toString())
                .image(item.get("image").toString())
                .link(item.get("link").toString())

                .build();
    }
    public static FasRecResponseDTO.ViewFashionTopDTO toFashionTopDTO(JSONObject fashionTop, FasRecResponseDTO.NaverItemDTO item){
        return  FasRecResponseDTO.ViewFashionTopDTO.builder()
                .type(fashionTop.get("type").toString())
                .color(fashionTop.get("color").toString())
                .content(fashionTop.get("content").toString())
                .image(item.getImage())
                .link(item.getLink())
                .build();
    }
     public static FasRecResponseDTO.ViewFashionBottomDTO toFashionBottomDTO(JSONObject fashionBottom, FasRecResponseDTO.NaverItemDTO item){
        return  FasRecResponseDTO.ViewFashionBottomDTO.builder()
                .type(fashionBottom.get("type").toString())
                .color(fashionBottom.get("color").toString())
                .content(fashionBottom.get("content").toString())
                .image(item.getImage())
                .link(item.getLink())
                .build();
    }
    public static FasRecResponseDTO.CreateResultDTO toCreateResultDTO(Gender gender, Type type, float height, float weight ,List<FasRecResponseDTO.ViewFashionTopDTO> fashionTops,List<FasRecResponseDTO.ViewFashionBottomDTO>fashionBottoms){
        return FasRecResponseDTO.CreateResultDTO.builder()
                .gender(gender)
                .type(type)
                .height(height)
                .weight(weight)
                .fashionTopDTOS(fashionTops)
                .fashionBottomDTOS(fashionBottoms)
                .build();
    }
}
