package vivio.spring.converter;

import vivio.spring.web.dto.SeaCloResponseDTO;

import java.util.List;

public class SeaCloConverter {
    public static SeaCloResponseDTO.SeaCloItemDTO toSeaCloItemDTO(Integer price,String title, String image, String link){
        return SeaCloResponseDTO.SeaCloItemDTO.builder()
                .title(title)
                .link(link)
                .image(image)
                .price(price)
                .build();
    }
    public static SeaCloResponseDTO.SeaCloListDTO toSeaCloListDTO(List<SeaCloResponseDTO.SeaCloItemDTO> items){
        return SeaCloResponseDTO.SeaCloListDTO.builder()
                .items(items)
                .build();
    }
}
