package vivio.spring.converter;

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
                .color(color)
                .type(type)
                .build();
    }
    public static FasRecResponseDTO.JoinResultDTO toJoinResultDTO(FashionRecommand fashionRecommand) {
        return FasRecResponseDTO.JoinResultDTO.builder()
                .fasRecId(fashionRecommand.getId())
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
}
