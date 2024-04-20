package vivio.spring.web.dto;

import lombok.*;

import javax.mail.Message;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptResponseDTO {
    private List<Choice> choices;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public  static class Choice {

        private int index;
        private GptRequestDTO.Message message;
    }
}
