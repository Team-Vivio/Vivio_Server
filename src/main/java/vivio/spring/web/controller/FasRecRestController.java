package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vivio.spring.apiPayLoad.ApiResponse;
import vivio.spring.config.TokenProvider;
import vivio.spring.converter.FasRecConverter;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.repository.FasRecRepository;
import vivio.spring.service.FasRecService.FasRecCommandService;
import vivio.spring.service.FasRecService.FasRecQueryService;
import vivio.spring.web.dto.FasRecResponseDTO;
import vivio.spring.web.dto.FasRecRequestDTO;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fashions")
public class FasRecRestController {
    private final FasRecCommandService fasRecCommandService;
    private final FasRecQueryService fasRecQueryService;
    private final TokenProvider tokenProvider;
    @PostMapping("fashionRecommand")
    public ApiResponse<FasRecResponseDTO.JoinResultDTO> join(@RequestBody @Valid FasRecRequestDTO.JoinDTO request, @RequestHeader("Authorization") String token){
        log.info(request.getImage());
        Long userId = tokenProvider.getUserIdFromToken(token);
        log.info(String.valueOf(userId));
        FashionRecommand fashionRecommand= fasRecCommandService.JoinFasRec(userId,request);

       return ApiResponse.onSuccess(FasRecConverter.toJoinResultDTO(fashionRecommand));
    }
    @GetMapping("fashionRecommand")
    public ApiResponse<FasRecResponseDTO.ViewAllResultDTO> get(@RequestHeader("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);
        List<FasRecResponseDTO.ViewListResultDTO> fashionRecommandList = fasRecQueryService.ViewFasRecList(userId);

        return ApiResponse.onSuccess(FasRecConverter.toViewAllResultDTO(fashionRecommandList));

    }

}
