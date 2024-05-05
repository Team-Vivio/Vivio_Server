package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.apiPayLoad.ApiResponse;
import vivio.spring.config.TokenProvider;
import vivio.spring.converter.FasCloConverter;
import vivio.spring.service.FasCloService.FasCloCommandService;
import vivio.spring.service.FasCloService.FasCloQueryService;
import vivio.spring.web.dto.FasCloRequestDTO;
import vivio.spring.web.dto.FasCloResponseDTO;
import vivio.spring.web.dto.FasRecRequestDTO;
import vivio.spring.web.dto.PerColRequestDTO;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/closets")
public class FasCloRestController {
    private final FasCloCommandService fasCloCommandService;
    private final FasCloQueryService fasCloQueryService;
    private final TokenProvider tokenProvider;
    @PostMapping("")
    ApiResponse<FasCloResponseDTO.CreateDTO> create(@RequestPart(value ="request", required = true) @Valid FasCloRequestDTO.CrateRequestDTO request, @RequestPart(value="top",required = true) @Valid List<MultipartFile> tops,@RequestPart(value="bottom",required = true) @Valid List<MultipartFile> bottoms,@RequestPart(value="outer",required = false) @Valid List<MultipartFile> outers) throws IOException {

        return ApiResponse.onSuccess(fasCloCommandService.createFasClo(request,tops,bottoms,outers));
    }
    @PostMapping("/closet")
    ApiResponse<FasCloResponseDTO.FasCloJoinDTO> join(@RequestHeader("Authorization") String token,@RequestBody @Valid FasCloRequestDTO.JoinRequestDTO request){
        Long userId = tokenProvider.getUserIdFromToken(token);
       log.info(String.valueOf(userId));
       return ApiResponse.onSuccess(fasCloCommandService.joinFasClo(request,userId));
    }
    @GetMapping("/closet")
    ApiResponse<FasCloResponseDTO.ViewListResultDTO> viewList(@RequestHeader("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        return ApiResponse.onSuccess(fasCloQueryService.viewList(userId));
    }
    @GetMapping("/closet/{fashionClosetId}")
    ApiResponse<FasCloResponseDTO.ViewItemDTO> viewItem(@RequestHeader("Authorization") String token,@PathVariable Long fashionClosetId){
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        return ApiResponse.onSuccess(fasCloQueryService.viewItem(userId,fashionClosetId));
    }
}
