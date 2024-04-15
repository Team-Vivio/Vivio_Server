package vivio.spring.service.UserService;

import jakarta.transaction.Transactional;
import vivio.spring.domain.User;
import vivio.spring.web.dto.UserRequestDTO;

import java.util.Optional;

public interface UserCommandService {
    @Transactional
    User joinUser(UserRequestDTO.JoinDto request);



    @Transactional
    String LoginUser(UserRequestDTO.LoginDTO request);

    void makeRandomNumber();

    String joinEmail(String email);

    void mailSend(String setFrom, String toMail, String title, String content);

    boolean CheckAuthNum(String email, String authNum);
}

