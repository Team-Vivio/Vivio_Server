package vivio.spring.web.controller;

import jakarta.validation.Valid;
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


    @GetMapping("/userInfo")
    public ApiResponse<UserResponseDTO.userinfoDTO> userInfoCheck(@RequestHeader("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);
        return ApiResponse.onSuccess(userQueryService.viewUserinfo(userId));
    }

}
