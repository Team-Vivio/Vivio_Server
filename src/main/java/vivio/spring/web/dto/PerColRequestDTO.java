package vivio.spring.web.dto;

import lombok.Getter;
import lombok.Setter;

public class PerColRequestDTO {

    @Getter
    @Setter
    public static class PerColJoinDTO{
        Integer gender;
        Integer personalColor;

    }

    @Getter
    @Setter
    public static class CreateDto{
        Integer gender;

    }
}
