package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.apiPayLoad.ApiResponse;

import vivio.spring.config.TokenProvider;
import vivio.spring.converter.PerColConverter;
import vivio.spring.domain.PersonalColor;
import vivio.spring.service.FasRecService.FasRecCommandService;
import vivio.spring.service.PerColService.PersonalColorCommandService;
import vivio.spring.service.PerColService.PersonalColorQueryService;
import vivio.spring.web.dto.FasRecResponseDTO;
import vivio.spring.web.dto.PerColRequestDTO;
import vivio.spring.web.dto.PerColResponseDTO;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/colors")

public class PerColorRestController {
    private final PersonalColorCommandService personalColorCommandService;
    private final TokenProvider tokenProvider;
    private final PersonalColorQueryService personalColorQueryService;
    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<PerColResponseDTO.CreateResponseDTO> createPersonalColor(@RequestPart(value ="request", required = true) @Valid PerColRequestDTO.CreateDto request, @RequestPart(value="image",required = true) @Valid MultipartFile file){
        log.info(request.getGender().toString());
        PerColResponseDTO.CreateResponseDTO personalColors=personalColorCommandService.CreatePerCol(request,file);
        return ApiResponse.onSuccess(personalColors);
    }
    @PostMapping("/personalColor")
    public ApiResponse<PerColResponseDTO.JoinResponseDTO>join(@RequestPart(value ="request", required = true) @Valid PerColRequestDTO.PerColJoinDTO request, @RequestPart(value="image",required = true) @Valid MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
       Long userId = tokenProvider.getUserIdFromToken(token);
       log.info(String.valueOf(userId));
        return ApiResponse.onSuccess( personalColorCommandService.JoinPerCol(request,userId,file));
    }
    @GetMapping("/personalColor")
    public ApiResponse<PerColResponseDTO.ViewAllResultDTO>getPersonalColors(@RequestHeader("Authorization") String token) throws IOException {
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        List<PerColResponseDTO.ViewListResultDTO> personalColorList=personalColorQueryService.ViewPerColList(userId);
        return ApiResponse.onSuccess(PerColConverter.toViewAllResultDTO(personalColorList));
    }
    @GetMapping("/personalColor/{personalColorId}")
    public ApiResponse<PerColResponseDTO.ViewResponseDTO> getPerSonalColor(@PathVariable("personalColorId") Long personalColorId, @RequestHeader("Authorization") String token) throws IOException {
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        PerColResponseDTO.ViewResponseDTO viewResponseDTO= personalColorQueryService.ViewPerCol(personalColorId,userId);
        return ApiResponse.onSuccess(viewResponseDTO);
    }


}
