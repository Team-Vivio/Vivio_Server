package vivio.spring.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FasCloRequestDTO {
    @Getter
    @Setter
    public static class CrateRequestDTO{
        Integer gender;
    }
    @Getter
    @Setter
    public static class JoinRequestDTO{
        List<JoinItemDTO> items;
    }
    @Getter
    @Setter
    public static class JoinItemDTO{
        String outer;
        String top;
        String bottom;
        String fashionName;
    }
}
