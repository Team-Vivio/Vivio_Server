package vivio.spring.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
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

public class UserRestController {
    private static Logger logger = LoggerFactory.getLogger(UserRestController.class);  //1
    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ApiResponse<UserResponseDTO.JoinResultDTO> join(@RequestBody @Valid UserRequestDTO.JoinDto request){

        log.info(request.getName());
        User user =userCommandService.joinUser(request);
        log.info(user.getName());
        return ApiResponse.onSuccess(UserConverter.toJoinResultDTO(user));
    }
    @PostMapping("/signin")
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
    @PostMapping("/findEmail")
    public ApiResponse<UserResponseDTO.emailFindResultDTO> emailFind(@RequestBody @Valid UserRequestDTO.EmailFindDTO request){
        UserResponseDTO.emailFindResultDTO email= userCommandService.FindEmail(request);

        if(email==null){
            return ApiResponse.onSuccess(null);

        }else{
            return ApiResponse.onSuccess(email);
        }
    }
    @PostMapping("/sendTempPassword")
    public ApiResponse<String> sendPassword(@RequestBody @Valid UserRequestDTO.TempPasswordDTO request){
        int result = userCommandService.TempPasswordSend(request);
        return switch (result) {
            case 0 -> ApiResponse.onSuccess("Suceess");
            case 1 -> ApiResponse.onFailure("EMAIL4001","이메일이 존재하지 않습니다",null);
            case 2 -> ApiResponse.onFailure("EMAIL4002","소셜로그인 계정입니다.", null);
            default -> null;
        };
    }

}
