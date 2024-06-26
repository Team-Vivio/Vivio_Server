package vivio.spring.converter;

import vivio.spring.domain.Bottom;
import vivio.spring.domain.ClothesOuter;
import vivio.spring.domain.Top;
import vivio.spring.domain.User;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Platform;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class UserConverter {

    public static UserResponseDTO.userinfoDTO toUserinfoDTO(User user){
        return UserResponseDTO.userinfoDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .build();
    }
    public static UserResponseDTO.JoinResultDTO toJoinResultDTO(User user) {
        return UserResponseDTO.JoinResultDTO.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static UserResponseDTO.closetResultDTO toClosetResultDTO(String image){
        return UserResponseDTO.closetResultDTO.builder()
                .image(image)
                .build();
    }
    public static Top toTop(String image,User user){
        return Top.builder()
                .image(image)
                .build();
    }
    public static Bottom toBottom(String image,User user){
        return Bottom.builder()
                .image(image)
                .user(user)
                .build();
    }
    public static ClothesOuter toOuter(String image){
        return ClothesOuter.builder()
                .image(image)
                .build();
    }
    public static UserResponseDTO.LoginResultDTO toLoginResultDTO(String token) {
        return UserResponseDTO.LoginResultDTO.builder()
                .token(token)
                .build();
    }
    public static UserResponseDTO.emailResultDTO toEmailResultDTO(String message){
        return UserResponseDTO.emailResultDTO.builder()
                .message(message)
                .build();
    }
    public static UserResponseDTO.emailCheckResultDTO toEmailCheckResultDTO(String Ok){
        return UserResponseDTO.emailCheckResultDTO.builder()
                .result(Ok)
                .build();
    }
    public static UserResponseDTO.closetItem toClosetItem(Long id, String image){
        return UserResponseDTO.closetItem.builder()
                .id(id)
                .image(image)
                .build();
    }

    public static UserResponseDTO.closetBringDTO toClosetBringDTO(List<UserResponseDTO.closetItem> closetItems){
        return UserResponseDTO.closetBringDTO.builder()
                .images(closetItems)
                .build();
    }
    public static User toUser(UserRequestDTO.JoinDto request){
        Gender gender = null;
        Platform platform =null;
        switch (request.getGender()){
            case 1:
                gender = Gender.male;
                break;
            case 2:
                gender = Gender.female;
        }
        switch (request.getPlatform()){
            case 1:
                platform = Platform.EMAIL;
                break;
            case 2:
                platform = Platform.KAKAO;
                break;
            case 3:
                platform = Platform.GOOGLE;
        }

            return User.builder()
                    .name(request.getName())
                    .gender(gender)
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .phoneNumber(request.getPhoneNumber())
                    .birthDate(request.getBirthDate())
                    .coin(100)
                    .platform(platform)
                    .build();
//        }else{
//            return User.builder()
//                    .name(request.getName())
//                    .email(request.getEmail())
//                    .build();
//        }

    }
}
