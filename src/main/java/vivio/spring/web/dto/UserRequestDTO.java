package vivio.spring.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vivio.spring.domain.enums.Gender;

import java.time.LocalDate;
import java.util.Date;

public class UserRequestDTO {
    @Getter
    @Setter
    public static class JoinDto{
        String name;
        //String nickname;
        String email;
        String password;
        String phoneNumber;
        Integer gender;
        Integer coin;
        Integer platform;
        LocalDate birthDate;

    }

    @Getter
    @Setter
    public static class UserInfoDTO{
        String name;
        String email;
        String phoneNumber;
        Gender gender;
        Integer coin;
    }
    @Getter
    @Setter
    public static class ClosetJoinDTO{
        String type;
    }
    @Getter
    @Setter
    public static class LoginDTO{
        String email;
        String password;
    }
    @Getter
    @Setter
    public static class EmailDTO{
        @Email
        private String email;
    }
    @Getter
    @Setter
    public  static class ViewClosetDTO{
        String type;
    }
    @Data
    public static class EmailCheckDTO{
        @Email
        private String email;
        private String authNum;
    }
    @Getter
    @Setter
    public static class EmailFindDTO{

        private String phoneNum;
        private LocalDate birthDate;
        private String name;
    }
    @Getter
    @Setter
    public static class TempPasswordDTO{
        private String email;
    }

    @Getter
    @Setter
    public static class ChangePasswordDTO{
        private String originalPassword;
        private String password;
    }

}
