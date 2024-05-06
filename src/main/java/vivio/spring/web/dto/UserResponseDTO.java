package vivio.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vivio.spring.domain.enums.Gender;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO{
        Long userId;
        LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDTO{
        String token;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class closetResultDTO{
        String image;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class closetBringDTO{
        List<closetItem> images;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class closetItem{
        Long id;
        String image;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class emailResultDTO{
        String message;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class emailCheckResultDTO{
        String result;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor

    public static class userinfoDTO{
        String name;
        String email;
        String phoneNumber;
        Gender gender;
        Integer coin;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class emailFindResultDTO{
        String email;
    }



}
