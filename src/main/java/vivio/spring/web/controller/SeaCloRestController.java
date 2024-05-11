package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.apiPayLoad.ApiResponse;
import vivio.spring.service.SeaCloService.SeaCloCommandService;
import vivio.spring.web.dto.SeaCloResponseDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SeaCloRestController {
    private final SeaCloCommandService seaCloCommandService;
    @PostMapping("/clothes")
    public ApiResponse<SeaCloResponseDTO.SeaCloListDTO> create(@RequestPart(value="image",required = true) @Valid MultipartFile image) throws IOException {


        return ApiResponse.onSuccess(seaCloCommandService.createSeaClo(image));
    }
}
