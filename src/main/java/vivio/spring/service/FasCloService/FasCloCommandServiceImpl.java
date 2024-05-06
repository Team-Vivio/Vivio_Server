package vivio.spring.service.FasCloService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.converter.FasCloConverter;
import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.FashionStyle;
import vivio.spring.domain.User;
import vivio.spring.domain.enums.Fashions;
import vivio.spring.domain.mapping.FashionClosetFashionStyle;
import vivio.spring.repository.FasCloFasStyRepository;
import vivio.spring.repository.FasCloRepository;
import vivio.spring.repository.FasStyRepository;
import vivio.spring.repository.UserRepository;
import vivio.spring.web.dto.FasCloRequestDTO;
import vivio.spring.web.dto.FasCloResponseDTO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasCloCommandServiceImpl implements FasCloCommandService{
    private final AmazonS3 amazonS3;
    private final FasStyRepository fasStyRepository;
    private final UserRepository userRepository;
    private final FasCloRepository fasCloRepository;
    private final FasCloFasStyRepository fasCloFasStyRepository;
    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    @Transactional
    public FasCloResponseDTO.FasCloJoinDTO joinFasClo(FasCloRequestDTO.JoinRequestDTO request, Long userId){
        Optional<User> userOptional=userRepository.findById(userId);
        User user= userOptional.get();
        FashionCloset fashionCloset = FasCloConverter.toFashionCloset(user);
        FashionCloset newFashionCloset=fasCloRepository.save(fashionCloset);
        for(FasCloRequestDTO.JoinItemDTO item : request.getItems()) {
            Optional<FashionStyle> fashionStyleOptional = fasStyRepository.findByName(item.getFashionName());
            FashionStyle fashionStyle = fashionStyleOptional.get();
            FashionClosetFashionStyle fashionClosetFashionStyle=FasCloConverter.toFashionClosetFashionStyle(newFashionCloset,fashionStyle,item);
            fasCloFasStyRepository.save(fashionClosetFashionStyle);
        }
        return FasCloConverter.toJoin(newFashionCloset);
    }
    @Override
    @Transactional
    public FasCloResponseDTO.CreateDTO createFasClo(FasCloRequestDTO.CrateRequestDTO request, List<MultipartFile> tops, List<MultipartFile> bottoms, List<MultipartFile> outers) throws IOException {

        List<FasCloResponseDTO.FasTagItemDTO> topFasItems = toFasCloItems(tops);
        List<FasCloResponseDTO.FasTagItemDTO> bottomFasItems = toFasCloItems(bottoms);
        List<FasCloResponseDTO.FasTagItemDTO> outerFasItems = toFasCloItems(outers);
        //레이블 별로 이미지 분류
        List<FasCloResponseDTO.FasCloItems> fasCloItems =DivideStyles(topFasItems, bottomFasItems, outerFasItems);

        List<FasCloResponseDTO.CreateItemDTO> results=MatchingFashion(fasCloItems);

        return FasCloConverter.toCreateDTO(results);
    }
    public List<FasCloResponseDTO.CreateItemDTO> MatchingFashion(List<FasCloResponseDTO.FasCloItems> fasCloItems) throws IOException {
        log.info("Matching Fashion");
         List<FasCloResponseDTO.CreateItemDTO> matchedItems = new ArrayList<>();
          Set<String> seenCombinations = new HashSet<>();
        for (FasCloResponseDTO.FasCloItems item : fasCloItems) {
            log.info("매칭중");
            List<MultipartFile> tops = item.getTops();
            List<MultipartFile> bottoms = item.getBottoms();
            List<MultipartFile> outers = item.getOuters() != null ? item.getOuters() : new ArrayList<>();

            for (MultipartFile top : tops) {
                log.info(String.valueOf(top));
                for (MultipartFile bottom : bottoms) {
                    log.info(String.valueOf(bottom));
                    if (outers.isEmpty()) {
                        // 아우터가 없는 경우의 조합 생성
                        String combinationKey = createCombinationKey(top, bottom, null);
                        if (!seenCombinations.contains(combinationKey)) {
                            matchedItems.add(createMatchedItem(top, bottom, null, item.getFashion().name()));
                            seenCombinations.add(combinationKey);
                        }
                    } else {
                        for (MultipartFile outer : outers) {
                            // 아우터가 있는 경우의 조합 생성
                            String combinationKey = createCombinationKey(top, bottom, outer);
                            if (!seenCombinations.contains(combinationKey)) {
                                matchedItems.add(createMatchedItem(top, bottom, outer, item.getFashion().name()));
                                seenCombinations.add(combinationKey);
                            }
                        }
                    }
                }
            }
        }
        return matchedItems;
    }
    private String createCombinationKey(MultipartFile top, MultipartFile bottom, MultipartFile outer) throws IOException {
    // Combines URLs of top, bottom, and outer to create a unique key for the combination
    StringBuilder keyBuilder = new StringBuilder();
    keyBuilder.append(top != null ? Arrays.hashCode(top.getBytes()) : "null");
    keyBuilder.append("-");
    keyBuilder.append(bottom != null ? Arrays.hashCode(bottom.getBytes()) : "null");
    keyBuilder.append("-");
    keyBuilder.append(outer != null ? Arrays.hashCode(outer.getBytes()) : "null");
    return keyBuilder.toString();
}
    private FasCloResponseDTO.CreateItemDTO createMatchedItem(MultipartFile top, MultipartFile bottom, MultipartFile outer, String fashionName) {
    String topUrl = uploadToS3(top);
    String bottomUrl = uploadToS3(bottom);
    log.info("탑 "+topUrl);
    log.info("하의 "+bottomUrl);
    String outerUrl = outer != null ? uploadToS3(outer) : null;
    Optional<FashionStyle> fashionStyleOptional = fasStyRepository.findByName(fashionName);
    FashionStyle fashionStyle = fashionStyleOptional.get();
    return FasCloResponseDTO.CreateItemDTO.builder()
        .outer(outerUrl)
        .top(topUrl)
        .bottom(bottomUrl)
        .fashionName(fashionName)
        .description(fashionStyle.getDescription())
        .build();
    }
    private String uploadToS3(MultipartFile file) {
    if (file == null) return null;
    try {
        String originalFileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, originalFileName, file.getInputStream(), metadata));
        return amazonS3.getUrl(bucket, originalFileName).toString();
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
    public List<FasCloResponseDTO.FasCloItems> DivideStyles(List<FasCloResponseDTO.FasTagItemDTO> tops, List<FasCloResponseDTO.FasTagItemDTO> bottoms, List<FasCloResponseDTO.FasTagItemDTO> outers) {
        List<FasCloResponseDTO.FasCloItems> fasCloItems = new ArrayList<>();

        for(int i=0; i< Fashions.values().length;i++){
            log.info(String.valueOf(Fashions.values()[i]));
            //street, casual, vintage, nomcore, modern, feminine, dandy, layered, classic, sporty 이 순서로 검사,
            int finalI = i;
            log.info(String.valueOf(i));
            List<MultipartFile> topList = tops.stream()
                .map(top -> top.getProbabilities().get(finalI) > 0.2 ? top.getImage() : null)
                .filter(Objects::nonNull) // null 값 제거
                .collect(Collectors.toList());

            List<MultipartFile> bottomList = bottoms.stream()
                .map(bottom -> bottom.getProbabilities().get(finalI) > 0.2 ? bottom.getImage() : null)
                .filter(Objects::nonNull) // null 값 제거
                .collect(Collectors.toList());

            List<MultipartFile> outerList = null;
            if (!outers.isEmpty()) {
                outerList = outers.stream()
                    .map(outer -> outer.getProbabilities().get(finalI) > 0.2 ? outer.getImage() : null)
                    .filter(Objects::nonNull) // null 값 제거
                    .collect(Collectors.toList());
            }

            fasCloItems.add(FasCloResponseDTO.FasCloItems.builder()
                    .tops(topList)
                    .fashion(Fashions.values()[i])
                    .bottoms(bottomList)
                    .outers(outerList).build());
        }

        return fasCloItems;
    }
    public List<FasCloResponseDTO.FasTagItemDTO> toFasCloItems(List<MultipartFile> files) {
        if(files != null && !files.isEmpty()) { // null 체크 추가
            // RestTemplate 인스턴스 생성
            RestTemplate restTemplate = new RestTemplate();
            JSONParser jsonParser = new JSONParser();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultiValueMap을 사용해 파일과 기타 필요한 값을 포함하는 body 생성

            return files.stream().map(
                    file -> {
                        ResponseEntity<String> response;
                        try {
                            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                            // HttpEntity 객체 생성
                            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                            byte[] fileBytes = file.getBytes(); // InputStream을 바이트 배열로 변환
                            ByteArrayResource byteArrayResource = new ByteArrayResource(fileBytes) {
                                @Override
                                public String getFilename() {
                                    return file.getOriginalFilename();
                                }
                            };
                            body.add("file", byteArrayResource);
                            response = restTemplate.exchange(
                                    "https://vivio.ngrok.io/fashion",
                                    HttpMethod.POST,
                                    requestEntity,
                                    String.class);

                        }catch (HttpClientErrorException e) {
                            // 여기서 e.getStatusCode()와 e.getResponseBodyAsString()을 사용하여 오류 상세 분석
                            // 적절한 오류 메시지로 로깅 또는 사용자에게 피드백
                            throw new RuntimeException("Request failed: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
                        } catch (java.io.IOException e) {
                            throw new RuntimeException(e);
                        }
                        // API 요청 및 응답

                        // 응답 바디를 JSON 객체로 파싱
                        if (response.getStatusCode() == HttpStatus.OK) {
                            try {
                                JSONObject jsonResponse = (JSONObject) jsonParser.parse(response.getBody());

                                ArrayList<Double> probabilities = (ArrayList<Double>) jsonResponse.get("probabilities");
                                return FasCloResponseDTO.FasTagItemDTO.builder()
                                        .image(file)
                                        .probabilities(probabilities)
                                        .build();
                            } catch (Exception ex) {
                                // JSON 파싱 중 오류 처리. 실제 사용 시 로깅 또는 적절한 예외 처리 필요
                                ex.printStackTrace();
                            }
                        }
                        return null;
                    }
            ).collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
}
