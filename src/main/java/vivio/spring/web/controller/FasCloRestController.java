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
import vivio.spring.web.dto.FasCloResponseDTO;
import vivio.spring.web.dto.PerColRequestDTO;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/closets")
public class FasCloRestController {
    @PostMapping("")
    ApiResponse<FasCloResponseDTO.CreateDTO> create(@RequestPart(value ="request", required = true) @Valid PerColRequestDTO.PerColJoinDTO request, @RequestPart(value="image",required = true) @Valid List<MultipartFile> file){

        return null;
    }
}
