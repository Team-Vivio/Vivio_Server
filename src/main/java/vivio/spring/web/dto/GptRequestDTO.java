package vivio.spring.web.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class GptRequestDTO {
    @Getter
    public static class RequstDTO {

        private String model;           // 모델명
        private List<Message> messages; // 질문들
        public RequstDTO(String model, String systemPrompt, String userPrompt) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("system",systemPrompt));
            this.messages.add(new Message("user",userPrompt));
        }
    }
    @Getter
    public static class Message {

    private String role;
    private String content;
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
}


