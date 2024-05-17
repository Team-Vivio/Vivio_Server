package vivio.spring.service.PerColService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.converter.PerColConverter;
import vivio.spring.domain.*;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Session;
import vivio.spring.domain.enums.Tone;
import vivio.spring.domain.mapping.ColorRecommendColor;
import vivio.spring.domain.mapping.HairColor;
import vivio.spring.repository.*;
import vivio.spring.web.dto.PerColRequestDTO;
import vivio.spring.web.dto.PerColResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class PersonalColorCommandServiceImpl implements PersonalColorCommandService {
    private final PerBeautyRepository perBeautyRepository;
    private final PerColRepository perColRepository;
    private final PerHairReposiotry perHairReposiotry;
    private final PerColRecRepository perColRecRepository;
    private final HairColorRepository hairColorRepository;
    private final BeautyColorRepository beautyColorRepository;
    private final ColorRecommendColorRepository colorRecommendColorRepository;
    private final ColorRepository colorRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    @Transactional
    public PerColResponseDTO.CreateResponseDTO CreatePerCol(PerColRequestDTO.CreateDto request, MultipartFile file){
        Session session = null;
        Gender gender = null;
        Tone tone=null;
        Optional<Hair> hairOptional = null;
        Optional<Beauty> beautyOptional = null;
        Optional<ColorRecommend> colorRecommendOptional=null;
        Hair hair = null;
        Beauty beauty = null;
        ColorRecommend colorRecommend  =null;

        switch (request.getGender()){
            case 1:
                gender = Gender.male;

                break;
            case 2:
                gender = Gender.female;

                break;
        }
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeaders 객체를 생성하고, Content-Type을 Multipart/Form-Data로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultiValueMap을 사용해 파일과 기타 필요한 값을 포함하는 body 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            // MultipartFile을 FileSystemResource로 변환하여 body에 추가
            if (file != null && !file.isEmpty()) {
                body.add("file", new InputStreamResource(file.getInputStream()) {
                    @Override
                    public long contentLength() {
                        return file.getSize();
                    }

                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
            });
            }
        } catch (IOException ex) {
            // 파일 변환 중 오류 처리. 실제 사용 시 로깅 또는 적절한 예외 처리 필요
            ex.printStackTrace();
            return null;
        }
        JSONParser jsonParser=new JSONParser();
        // HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // API 요청 및 응답
        ResponseEntity<String> response = restTemplate.exchange(
                "https://vivio.ngrok.io/predict",
                HttpMethod.POST,
                requestEntity,
                String.class);

        // 응답 바디를 JSON 객체로 파싱
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JSONObject jsonResponse = (JSONObject) jsonParser.parse(response.getBody());
                String clas = (String)jsonResponse.get("class");
                log.info(clas);
                double confidenceScore = (Double)jsonResponse.get("confidence_score");
                switch (clas){
                    case "Spring":
                        session=Session.spring;
                        tone=Tone.warm;
                        if(gender==Gender.male){
                            hairOptional = perHairReposiotry.findById(2L);
                            beautyOptional = perBeautyRepository.findById(2L);
                            colorRecommendOptional = perColRecRepository.findById(2L);
                        }else{
                             hairOptional = perHairReposiotry.findById(6L);
                             beautyOptional = perBeautyRepository.findById(6L);
                            colorRecommendOptional = perColRecRepository.findById(6L);
                        }
                        break;
                    case "Summer":
                        session=Session.summer;
                        tone=Tone.cool;
                        if(gender==Gender.male){
                            hairOptional = perHairReposiotry.findById(3L);
                            beautyOptional = perBeautyRepository.findById(3L);
                            colorRecommendOptional = perColRecRepository.findById(3L);
                        }else{
                             hairOptional = perHairReposiotry.findById(7L);
                             beautyOptional = perBeautyRepository.findById(7L);
                            colorRecommendOptional = perColRecRepository.findById(7L);
                        }
                        break;
                    case "Fall":
                        session=Session.fall;
                        tone=Tone.warm;
                        if(gender==Gender.male){
                            hairOptional = perHairReposiotry.findById(4L);
                            beautyOptional = perBeautyRepository.findById(4L);
                            colorRecommendOptional = perColRecRepository.findById(4L);
                        }else{
                             hairOptional = perHairReposiotry.findById(8L);
                             beautyOptional = perBeautyRepository.findById(8L);
                            colorRecommendOptional = perColRecRepository.findById(8L);
                        }
                    case "Winter":
                        session=Session.winter;
                        tone=Tone.cool;
                        if(gender==Gender.male){
                            hairOptional = perHairReposiotry.findById(5L);
                            beautyOptional = perBeautyRepository.findById(5L);
                            colorRecommendOptional = perColRecRepository.findById(5L);
                        }else{
                             hairOptional = perHairReposiotry.findById(9L);
                             beautyOptional = perBeautyRepository.findById(9L);
                            colorRecommendOptional = perColRecRepository.findById(9L);
                        }
                        break;
                }


            } catch (Exception ex) {
                // JSON 파싱 중 오류 처리. 실제 사용 시 로깅 또는 적절한 예외 처리 필요
                ex.printStackTrace();
            }
        }

        //헤어, 퍼스널 컬러 들의 컬러 리스트 찾기
        hair=hairOptional.get();
        beauty=beautyOptional.get();
        colorRecommend=colorRecommendOptional.get();
        Optional<HairColor> hairColorOptional;
        Optional<ColorRecommendColor> colorRecommendColorOptional;


        ColorRecommendColor colorRecommendColor;
        List<HairColor> hairColors=hairColorRepository.findAllByHair(hair);
        List<ColorRecommendColor> ColorRecommendColors=colorRecommendColorRepository.findAllByColorRecommend(colorRecommend);

        List<PerColResponseDTO.ColorDTO> colorHair=hairColors.stream()
                .map(hairColor ->
                        PerColConverter.toColorDTO(hairColor.getColor())).collect(Collectors.toList());
         List<PerColResponseDTO.ColorDTO> colorRecommands=ColorRecommendColors.stream()
                .map(colorRecommendColors -> PerColConverter.toColorDTO(colorRecommendColors.getColor())).collect(Collectors.toList());

        PerColResponseDTO.ViewHairDTO viewHairDTO= PerColConverter.toViewHairDTO(hair,colorHair);
        PerColResponseDTO.ViewPersonalColorDTO viewPersonalColorDTO=PerColConverter.toViewPersonalColorDTO(colorRecommend,colorRecommands);
        PerColResponseDTO.ViewBeautyDTO viewBeautyDTO=PerColConverter.toViewBeautyDTO(beauty);
        return PerColConverter.toCreateResult(gender,session,tone,viewHairDTO,viewBeautyDTO,viewPersonalColorDTO);


    }
   @Override
    @Transactional
    public PerColResponseDTO.JoinResponseDTO JoinPerCol(PerColRequestDTO.PerColJoinDTO request, Long userId, MultipartFile file) throws IOException {
        // 랜덤 파일 이름 생성
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String randomFileName = UUID.randomUUID().toString() + extension;

        // 아마존 S3 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, randomFileName, file.getInputStream(), metadata);
        String image = amazonS3.getUrl(bucket, randomFileName).toString();

        Optional<User> userOptional = userRepository.findById(userId);
        Gender gender = null;
        Tone tone = null;
        Session session = null;
        switch(request.getGender()){
            case 1:
                gender = Gender.male;
                break;
            case 2:
                gender = Gender.female;
                break;
        }
        switch (request.getPersonalColor()){
            case 1:
                session = Session.spring;
                tone = Tone.warm;
                break;
            case 2:
                session = Session.summer;
                tone = Tone.cool;
                break;
            case 3:
                session = Session.fall;
                tone = Tone.warm;
                break;
            case 4:
                session = Session.winter;
                tone = Tone.cool;
                break;
        }
        Optional<Hair> hairOptional = null;
        Optional<Beauty> beautyOptional = null;
        Optional<ColorRecommend> colorRecommendOptional = null;
        switch (session){
            case spring:
                if (gender == Gender.male) {
                    hairOptional = perHairReposiotry.findById(2L);
                    beautyOptional = perBeautyRepository.findById(2L);
                    colorRecommendOptional = perColRecRepository.findById(2L);
                } else {
                    hairOptional = perHairReposiotry.findById(6L);
                    beautyOptional = perBeautyRepository.findById(6L);
                    colorRecommendOptional = perColRecRepository.findById(6L);
                }
                break;
            case summer:
                if (gender == Gender.male) {
                    hairOptional = perHairReposiotry.findById(3L);
                    beautyOptional = perBeautyRepository.findById(3L);
                    colorRecommendOptional = perColRecRepository.findById(3L);
                } else {
                    hairOptional = perHairReposiotry.findById(7L);
                    beautyOptional = perBeautyRepository.findById(7L);
                    colorRecommendOptional = perColRecRepository.findById(7L);
                }
                break;
            case fall:
                if (gender == Gender.male) {
                    hairOptional = perHairReposiotry.findById(4L);
                    beautyOptional = perBeautyRepository.findById(4L);
                    colorRecommendOptional = perColRecRepository.findById(4L);
                } else {
                    hairOptional = perHairReposiotry.findById(8L);
                    beautyOptional = perBeautyRepository.findById(8L);
                    colorRecommendOptional = perColRecRepository.findById(8L);
                }
                break;
            case winter:
                if (gender == Gender.male) {
                    hairOptional = perHairReposiotry.findById(5L);
                    beautyOptional = perBeautyRepository.findById(5L);
                    colorRecommendOptional = perColRecRepository.findById(5L);
                } else {
                    hairOptional = perHairReposiotry.findById(9L);
                    beautyOptional = perBeautyRepository.findById(9L);
                    colorRecommendOptional = perColRecRepository.findById(9L);
                }
                break;
        }
        Hair hair = hairOptional.get();
        Beauty beauty = beautyOptional.get();
        ColorRecommend colorRecommend = colorRecommendOptional.get();
        User user = userOptional.get();
        PersonalColor newPersonalColor = PerColConverter.toPersonalColor(gender, session, tone, image, hair, beauty, colorRecommend, user);
        perColRepository.save(newPersonalColor);
        return PerColConverter.toJoinResponseDTO(newPersonalColor);
    }

}


