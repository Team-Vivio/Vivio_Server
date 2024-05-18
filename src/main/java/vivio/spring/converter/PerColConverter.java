package vivio.spring.converter;

import vivio.spring.domain.*;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Session;
import vivio.spring.domain.enums.Tone;
import vivio.spring.web.dto.PerColResponseDTO;

import java.util.List;

public class PerColConverter {

    public static PerColResponseDTO.CreateResponseDTO toCreateResult(Gender gender, Session session, Tone tone, PerColResponseDTO.ViewHairDTO viewHairDTO,PerColResponseDTO.ViewBeautyDTO viewBeautyDTO,PerColResponseDTO.ViewPersonalColorDTO viewPersonalColorDTO){

        return PerColResponseDTO.CreateResponseDTO.builder()
                .gender(gender)
                .session(session)
                .tone(tone)
                .hair(viewHairDTO)
                .beauty(viewBeautyDTO)
                .personalColor(viewPersonalColorDTO)
                .build();

    }
    public static PerColResponseDTO.ViewHairDTO toViewHairDTO(Hair hair,List<PerColResponseDTO.ColorDTO> hairColors){
        return PerColResponseDTO.ViewHairDTO.builder()
                .description(hair.getDescription())
                .colors(hairColors)
                .build();
    }
    public static PerColResponseDTO.ViewBeautyDTO toViewBeautyDTO(Beauty beauty){
        return PerColResponseDTO.ViewBeautyDTO.builder()
                .description(beauty.getDescription())

                .build();
    }
    public static PerColResponseDTO.ViewPersonalColorDTO toViewPersonalColorDTO(ColorRecommend color,List<PerColResponseDTO.ColorDTO> colors){
        return PerColResponseDTO.ViewPersonalColorDTO.builder()
                .colors(colors)
                .description(color.getDescription())
                .build();
    }
    public static PerColResponseDTO.ColorDTO toColorDTO(Color color){
        return PerColResponseDTO.ColorDTO.builder()
                .code(color.getCode())
                .build();
    }
    public static PersonalColor toPersonalColor(Gender gender,Session session, Tone tone,String image,Hair hair,Beauty beauty,ColorRecommend color,User user){
        return PersonalColor.builder()
                .gender(gender)
                .session(session)
                .tone(tone)
                .user(user)
                .hair(hair)
                .beauty(beauty)
                .image(image)
                .colorRecommend(color)
                .build();
    }
    public static PerColResponseDTO.JoinResponseDTO toJoinResponseDTO(PersonalColor personalColor){
        return PerColResponseDTO.JoinResponseDTO.builder()
                .PerColId(personalColor.getId())
                .createdAt(personalColor.getCreatedAt())
                .build();
    }
    public static PerColResponseDTO.ViewAllResultDTO toViewAllResultDTO(List<PerColResponseDTO.ViewListResultDTO> personalColors){
        return PerColResponseDTO.ViewAllResultDTO.builder()
                .viewListResultDTOS(personalColors)
                .build();
    }
    public static PerColResponseDTO.ViewListResultDTO toViewListResultDTO(PersonalColor personalColor){
        return PerColResponseDTO.ViewListResultDTO.builder()
                .PerColId(personalColor.getId())
                .image(personalColor.getImage())
                .session(personalColor.getSession())
                .tone(personalColor.getTone())
                .build();
    }
    public static PerColResponseDTO.ViewResponseDTO toViewResult(String name ,String image,Gender gender, Session session, Tone tone, PerColResponseDTO.ViewHairDTO viewHairDTO,PerColResponseDTO.ViewBeautyDTO viewBeautyDTO,PerColResponseDTO.ViewPersonalColorDTO viewPersonalColorDTO){

        return PerColResponseDTO.ViewResponseDTO.builder()
                .image(image)
                .name(name)
                .gender(gender)
                .session(session)
                .tone(tone)
                .hair(viewHairDTO)
                .beauty(viewBeautyDTO)
                .personalColor(viewPersonalColorDTO)
                .build();

    }
}
