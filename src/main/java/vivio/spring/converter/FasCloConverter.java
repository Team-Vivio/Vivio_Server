package vivio.spring.converter;

import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.FashionStyle;
import vivio.spring.domain.User;
import vivio.spring.domain.mapping.FashionClosetFashionStyle;
import vivio.spring.web.dto.FasCloRequestDTO;
import vivio.spring.web.dto.FasCloResponseDTO;
import vivio.spring.web.dto.FasRecResponseDTO;

import java.util.List;

public class FasCloConverter {
    public static FasCloResponseDTO.CreateItemDTO toCreateItemDTO(String outer,String top,String bottom,String fashionName,String description){
        return FasCloResponseDTO.CreateItemDTO.builder()
                .outer(outer)
                .top(top)
                .bottom(bottom)
                .fashionName(fashionName)
                .description(description).build();
    }
    public static FasCloResponseDTO.CreateDTO toCreateDTO(List<FasCloResponseDTO.CreateItemDTO> items){
        return FasCloResponseDTO.CreateDTO.builder()
                .items(items)
                .build();
    }
    public static FasCloResponseDTO.FasCloJoinDTO toJoin(FashionCloset fashionCloset){
        return FasCloResponseDTO.FasCloJoinDTO.builder()
                .id(fashionCloset.getId())
                .createdAt(fashionCloset.getCreatedAt())
                .build();
    }
    public static FashionCloset toFashionCloset(User user){
        return FashionCloset.builder()
                .user(user)

                .build();
    }
    public static FashionClosetFashionStyle toFashionClosetFashionStyle(FashionCloset fashionCloset,FashionStyle fashionStyle, FasCloRequestDTO.JoinItemDTO item){
        return FashionClosetFashionStyle.builder()
                .fashionCloset(fashionCloset)
                .fashionStyle(fashionStyle)
                .top(item.getTop())
                .bottom(item.getBottom())
                .outer(item.getOuter())
                .build();
    }

    public static FasCloResponseDTO.ViewListItemDTO toViewListItem(FashionClosetFashionStyle fashionClosetFashionStyle) {
        return FasCloResponseDTO.ViewListItemDTO.builder()
                .id(fashionClosetFashionStyle.getFashionCloset().getId())
                .image(fashionClosetFashionStyle.getTop())
                .style(fashionClosetFashionStyle.getFashionStyle().getName())
                .build();
    }

    public static FasCloResponseDTO.ViewListResultDTO toViewList(List<FasCloResponseDTO.ViewListItemDTO> items) {
        return FasCloResponseDTO.ViewListResultDTO.builder()
                .items(items)
                .build();
    }
    public static FasCloResponseDTO.ViewItemDTO toViewItemDTO(FashionCloset fashionCloset,List<FasCloResponseDTO.ItemDTO> items){
        return FasCloResponseDTO.ViewItemDTO.builder()
                .id(fashionCloset.getId())
                .items(items)
                .build();
    }
    public static FasCloResponseDTO.ItemDTO toItemDTO(FashionClosetFashionStyle fashionClosetFashionStyle){
        return FasCloResponseDTO.ItemDTO.builder()
                .top(fashionClosetFashionStyle.getTop())
                .bottom(fashionClosetFashionStyle.getBottom())
                .outer(fashionClosetFashionStyle.getOuter())
                .id(fashionClosetFashionStyle.getId())
                .fashionName(fashionClosetFashionStyle.getFashionStyle().getName())
                .description(fashionClosetFashionStyle.getFashionStyle().getDescription())
                .build();
    }
}
