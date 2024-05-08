package vivio.spring.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.apiPayLoad.ApiResponse;
import vivio.spring.apiPayLoad.code.status.ErrorStatus;
import vivio.spring.apiPayLoad.exception.handler.UserHandler;
import vivio.spring.config.TokenProvider;
import vivio.spring.converter.UserConverter;
import vivio.spring.domain.User;
import vivio.spring.repository.UserRepository;
import vivio.spring.service.UserService.UserCommandService;
import vivio.spring.service.UserService.UserQueryService;
import vivio.spring.service.UserService.UserQueryServiceImpl;
import vivio.spring.web.dto.UserRequestDTO;
import vivio.spring.web.dto.UserResponseDTO;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

public class UserRestController {
    private static Logger logger = LoggerFactory.getLogger(UserRestController.class);  //1
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;


    @PostMapping("/emailcheck")
    public ApiResponse<UserResponseDTO.emailResultDTO>emailcheck(@RequestBody @Valid UserRequestDTO.EmailDTO request){
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent())
            return ApiResponse.onSuccess(UserConverter.toEmailResultDTO("false"));
        else
            return ApiResponse.onSuccess(UserConverter.toEmailResultDTO("true"));
    }
    @PostMapping("/closet")
    public ApiResponse<UserResponseDTO.closetResultDTO>closet(@RequestHeader("Authorization") String token,@RequestPart(value="request",required = true) @Valid UserRequestDTO.ClosetJoinDTO request,@RequestPart(value="image",required = true) @Valid MultipartFile file) throws IOException {
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        String image= userCommandService.JoinClothes(userId,request,file);
        return ApiResponse.onSuccess(UserConverter.toClosetResultDTO(image));
    }
    @GetMapping ("/closet/{type}")
    public ApiResponse<UserResponseDTO.closetBringDTO>viewCloset(@RequestHeader("Authorization") String token,@PathVariable("type") String type){
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));


        return ApiResponse.onSuccess(userQueryService.viewClosets(userId,type));
    }

    @DeleteMapping("/closet/{type}/{id}")
    public ApiResponse<String>deleteCloth(@RequestHeader("Authorization") String token, @PathVariable("type") String type, @PathVariable("id") Long id){
        Long userId = tokenProvider.getUserIdFromToken(token);
        boolean result = userCommandService.DeleteCloth(userId, type, id);
        if(result)
            return ApiResponse.onSuccess("삭제에 성공했습니다");
        else
            return ApiResponse.onFailure("CLOSET4005","삭제에 실패했습니다",null);
    }

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


    @GetMapping("/userInfo")
    public ApiResponse<UserResponseDTO.userinfoDTO> userInfoCheck(@RequestHeader("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);
        return ApiResponse.onSuccess(userQueryService.viewUserinfo(userId));
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

    @PostMapping("/changePassword")
    public ApiResponse<String> changePassword(@RequestHeader("Authorization") String token,@RequestBody @Valid UserRequestDTO.ChangePasswordDTO request){
        String password = request.getPassword();
        Long userId = tokenProvider.getUserIdFromToken(token);
        boolean result = userCommandService.ChangePassword(userId, request);
        if(result)
            return ApiResponse.onSuccess("Sucess");
        else
            return ApiResponse.onFailure("USER4003","비밀번호가 일치하지 않습니다",null);
    }

}
