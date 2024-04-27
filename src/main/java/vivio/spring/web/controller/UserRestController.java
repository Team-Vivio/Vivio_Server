package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import vivio.spring.apiPayLoad.ApiResponse;
import vivio.spring.apiPayLoad.code.status.ErrorStatus;
import vivio.spring.apiPayLoad.exception.handler.UserHandler;
import vivio.spring.converter.UserConverter;
import vivio.spring.domain.User;
import vivio.spring.service.UserService.UserCommandService;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRestController {
    private static Logger logger = LoggerFactory.getLogger(UserRestController.class);  //1
    private final UserCommandService userCommandService;

    @PostMapping("/siginup")
    public ApiResponse<UserResponseDTO.JoinResultDTO> join(@RequestBody @Valid UserRequestDTO.JoinDto request){

        log.info(request.getName());
        User user =userCommandService.joinUser(request);
        log.info(user.getName());
        return ApiResponse.onSuccess(UserConverter.toJoinResultDTO(user));
    }
    @PostMapping("/siginin")
    public ApiResponse<UserResponseDTO.LoginResultDTO> login(@RequestBody @Valid UserRequestDTO.LoginDTO request){
        String token= userCommandService.LoginUser(request);
        return ApiResponse.onSuccess(UserConverter.toLoginResultDTO(token));
    }

    @PostMapping("/sendEmail")
    public ApiResponse<UserResponseDTO.emailResultDTO> emailAuth(@RequestBody @Valid UserRequestDTO.EmailDTO request){
        String authNumber=userCommandService.joinEmail(request.getEmail());
        return ApiResponse.onSuccess(UserConverter.toEmailResultDTO(authNumber));
    }
    @PostMapping("/checkEmail")
    public ApiResponse<UserResponseDTO.emailCheckResultDTO> emailCheck(@RequestBody @Valid UserRequestDTO.EmailCheckDTO request){
        Boolean state=userCommandService.CheckAuthNum(request.getEmail(),request.getAuthNum());
        if (state){
            return ApiResponse.onSuccess(UserConverter.toEmailCheckResultDTO("Success"));
        }else{
            throw new UserHandler(ErrorStatus.EMAIL_EXCEPTION);
        }
    }

}
