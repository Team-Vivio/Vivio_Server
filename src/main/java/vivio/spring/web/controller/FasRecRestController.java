package vivio.spring.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Base64;
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
    @GetMapping("/fashionRecommand/{fashionRecommandId}")
    public ApiResponse<FasRecResponseDTO.ViewResultDTO> getFashion(@PathVariable("fashionRecommandId") Long id){
        FashionRecommand fashionRecommand = fasRecQueryService.ViewFasRec(id);

        return ApiResponse.onSuccess(fasRecQueryService.ViewFasRecResult(fashionRecommand));
    }
    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<FasRecResponseDTO.CreateResultDTO> createFashion(@RequestPart(value ="request", required = true) @Valid FasRecRequestDTO.FasRecCreateDTO request, @RequestPart(value="image",required = false) @Valid MultipartFile file){
        try{
            byte[] bytes =file.getBytes();
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);
            FasRecResponseDTO.CreateResultDTO fashion=fasRecCommandService.CreateFasRec(request,base64Encoded);
            return ApiResponse.onSuccess(fashion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
