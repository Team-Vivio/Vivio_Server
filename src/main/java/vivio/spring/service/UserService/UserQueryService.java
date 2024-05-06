package vivio.spring.service.UserService;

import jakarta.transaction.Transactional;
import vivio.spring.domain.User;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

import java.util.List;

public interface UserQueryService {

    @Transactional
    UserResponseDTO.closetBringDTO viewClosets(Long userId, String type);

    @Transactional
    UserResponseDTO.userinfoDTO viewUserinfo(Long userId);


}
