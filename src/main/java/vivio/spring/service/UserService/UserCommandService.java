package vivio.spring.service.UserService;

import jakarta.transaction.Transactional;
import vivio.spring.domain.User;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

import java.util.Optional;

public interface UserCommandService {
    @Transactional
    User joinUser(UserRequestDTO.JoinDto request);



    @Transactional
    String LoginUser(UserRequestDTO.LoginDTO request);
    @Transactional

    void makeRandomNumber();
    @Transactional

    String joinEmail(String email);
    @Transactional

    void mailSend(String setFrom, String toMail, String title, String content);
    @Transactional

    boolean CheckAuthNum(String email, String authNum);

    @Transactional
    UserResponseDTO.emailFindResultDTO FindEmail(UserRequestDTO.EmailFindDTO request);

    @Transactional
    int TempPasswordSend(UserRequestDTO.TempPasswordDTO requst);
}

