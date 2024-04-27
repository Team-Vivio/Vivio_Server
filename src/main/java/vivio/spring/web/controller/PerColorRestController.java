package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.apiPayLoad.ApiResponse;

import vivio.spring.service.FasRecService.FasRecCommandService;
import vivio.spring.service.PerColService.PersonalColorCommandService;
import vivio.spring.web.dto.PerColRequestDTO;
import vivio.spring.web.dto.PerColResponseDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/colors")

public class PerColorRestController {
    private final PersonalColorCommandService personalColorCommandService;
    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<PerColResponseDTO.CreateResponseDTO> createPersonalColor(@RequestPart(value ="request", required = true) @Valid PerColRequestDTO.CreateDto request, @RequestPart(value="image",required = false) @Valid MultipartFile file){

        PerColResponseDTO.CreateResponseDTO personalColors=personalColorCommandService.CreatePerCol(request,file);
        return ApiResponse.onSuccess(personalColors);
    }
    @PostMapping("/personalColor")
    public ApiResponse<PerColResponseDTO.JoinResponseDTO>join(@RequestBody @Valid PerColRequestDTO.PerColJoinDTO requst){
        return null;
    }


}
