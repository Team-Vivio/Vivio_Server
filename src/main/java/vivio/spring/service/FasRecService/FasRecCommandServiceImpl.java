package vivio.spring.service.FasRecService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vivio.spring.converter.FasRecConverter;
import vivio.spring.domain.*;
import vivio.spring.domain.enums.Gender;
import vivio.spring.domain.enums.Platform;
import vivio.spring.domain.enums.Type;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.repository.*;
import vivio.spring.web.dto.FasRecRequestDTO;
import vivio.spring.web.dto.FasRecResponseDTO;
import vivio.spring.web.dto.GptRequestDTO;
import vivio.spring.web.dto.GptResponseDTO;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasRecCommandServiceImpl implements FasRecCommandService{
    private final UserRepository userRepository;
    private final FasColorRepository fasColorRepository;
    private final FasTypeRepository fasTypeRepository;
    private final FasTopRepository fasTopRepository;
    private final FasBottomRepository fasBottomRepository;
    private final FasRecRepository fasRecRepository;
    private FashionColor fashionColor;
    private FashionType fashionType;
    private FasRecRequestDTO fasRecRequestDTO;
    @Value("${spring.fit.token}")
    private String fitToken;
    @Value("${openai.url}")
    private String openAiUrl;
    @Value("${openai.secret-key}")
    private String gptSecretKey;
    @Value("${openai.model}")
    private String gptModel;
    @Value("${naver.client-id}")
    private String naverClientId;
    @Value("${naver.client-secret}")
    private String naverClientSecret;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    @Transactional

    public FashionRecommand JoinFasRec (long userId , FasRecRequestDTO.JoinDTO request){
        FashionRecommand newFashionRecommand= FasRecConverter.toFashionRecommand(request);
        Optional<User> isUser = userRepository.findById(userId);
        if(isUser.isPresent()){
            User user = isUser.get();
            newFashionRecommand.setUser(user);
        }
        //상의 배열 저장하기
        List<FashionTop> fashionTopList = request.getFashionTops().stream()
                .map(fashion ->{
                    Optional<FashionColor> fasColorOptional = fasColorRepository.findByName(fashion.getColor());
                    if(fasColorOptional.isPresent()){
                        fashionColor= fasColorOptional.get();
                    }else{
                        FashionColor color= FashionColor.builder()
                                .name(fashion.getColor())
                                .build();
                        fasColorRepository.save(color);
                        fashionColor = color;
                    }
                    Optional<FashionType> fashionTypeOptional = this.fasTypeRepository.findByName(fashion.getType());
                    if(fashionTypeOptional.isPresent()){
                        fashionType= fashionTypeOptional.get();
                    }else{
                        FashionType type = FashionType.builder()
                                        .name(fashion.getType())
                                        .build();
                        fasTypeRepository.save(type);
                        fashionType = type;
                    }

                    FashionTop fashionTop = FasRecConverter.toFashionTop(newFashionRecommand,fashion,fashionColor,fashionType);
                    return fashionTop;

                }).collect(Collectors.toList());
        fasTopRepository.saveAll(fashionTopList);
        //하의 배열 저장하기
        List<FashionBottom> fashionBottomList = request.getFashionBottoms().stream()
                .map(fashion ->{
                    Optional<FashionColor> fasColorOptional = fasColorRepository.findByName(fashion.getColor());
                    if(fasColorOptional.isPresent()){
                        fashionColor= fasColorOptional.get();
                    }else{
                        FashionColor color= FashionColor.builder()
                                .name(fashion.getColor())
                                .build();
                        fasColorRepository.save(color);
                        fashionColor = color;
                    }
                    Optional<FashionType> fashionTypeOptional = this.fasTypeRepository.findByName(fashion.getType());
                    if(fashionTypeOptional.isPresent()){
                        fashionType= fashionTypeOptional.get();
                    }else{
                        FashionType type = FashionType.builder()
                                .name(fashion.getType())
                                .build();
                        fasTypeRepository.save(type);
                        fashionType = type;
                    }

                    FashionBottom fashionBottom= FasRecConverter.toFashionBottom(newFashionRecommand,fashion,fashionColor,fashionType);
                    return fashionBottom  ;

                }).collect(Collectors.toList());
        fasBottomRepository.saveAll(fashionBottomList);

        fasRecRepository.save(newFashionRecommand);

        return newFashionRecommand;
    }
    @Override
    @Transactional
    public FasRecResponseDTO.CreateResultDTO CreateFasRec(FasRecRequestDTO.FasRecCreateDTO request, String image) throws MessagingException, IOException, ParseException {
        Double fatPercent;
        WebClient webClient = WebClient.builder().baseUrl("http://www.fitimage.io/api/api_fat_predict/").build();
        WebClient gptWebClient = WebClient.builder().baseUrl(openAiUrl).build();
        String genderName=null;
        Gender gender = null;
        Type type = null;
        switch (request.getGender()){
            case 1:
                gender = Gender.male;
                genderName="남성";
                break;
            case 2:
                gender = Gender.female;
                genderName="여성";
                break;
        }
         switch (request.getType()) {
            case 1:
                type = Type.slim;
                break;
            case 2:
                type = Type.normal;
                break;
            case 3:
                type = Type.fat;
                break;
        }
        if(image!= null) {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("image", image);
            bodyMap.put("gender", gender);
            bodyMap.put("token", fitToken);
            Mono<Double> fatPercentage = webClient.post()
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(jsonString -> parseFatFromJson(jsonString));
            //체지방률 받아옴
            fatPercent = fatPercentage.block();
        }else{
            fatPercent = null;
        }

        //GPT 사용
        String systemPrompt =" e in the request is one of slim, normal, and fat selected by the user. FatPercent may or may not be valuable as body fat percentage. The first thing to consider when analyzing body type is BMI, followed by body fat percentage.\n" +
                "Jsom data probed should be given in the following format. Top recommendations and bottom recommendations should be stored separately in the form of arrangement. Fashion Tops that save top recommendations are top and Fashion Bottom is bottom. Each should provide at least 1~3 pieces and color, type, and content should be provided in Korean. Also, in the content, you need to explain why the user's body shape matches the color of this outfit, and explain clearly and in detail why it is this outfit. However, you need to change the slim to skinny, normal to normal, and fat to plump. Color and type should be provided one for each arrangement element, and only JSON value should be provided" +


                "Request information\n" +
                "{\n" +
                "\"gender\":\"male\",\n" +
                "\"height\" : 170,\n" +
                "\"weight\" : 80,\n" +
                "\"BMI\" : 27.68,\n" +
                "\"bodyType\" : normal,\n" +
                "\"fatPercent\" : 9.541653633117676,\n" +
                "}\n" +
                "JSON data provided:{\n" +
                "\"fashionTops\":[{\n" +
                "        \n" +
                "        \"color\": \"색깔\",\n" +
                "        \"type\":\"후드티\",\n" +
                "        \"content\": \"사용자의 체형에는 이러한 색깔과 이러한 후드티가 더 돋보일 수 있게합니다\"\n" +
                "    },\n" +
                "    {\n" +
                "        \n" +
                "        \"color\": \"빨간색\",\n" +
                "        \"type\":\"맨투맨\",\n" +
                "        \"content\": \"노멀하고 밝은 맨투맨를 추천합니다.\"\n" +
                "    }],\n" +
                "    \"fashionBottoms\":[{\n" +
                "        \n" +
                "        \"color\": \"파란색\",\n" +
                "        \"type\" : \"청바지\",\n" +
                "        \"content\" : \"밝은 청바지를 추천합니다.\"\n" +
                "    }]\n" +
                "\n" +
                "}";
        double bmi = request.getWeight() / (request.getHeight()*request.getHeight());
        String userPrompt="{\n" +
                "\"gender\":\""+gender+"\",\n" +
                "\"height\" :"+ request.getHeight()+",\n" +
                "\"weight\" :"+request.getWeight()+",\n" +
                "\"BMI\" : "+bmi+",\n" +
                "\"bodyType\" : "+type+",\n" +
                "\"fatPercent\" : "+fatPercent+ ",\n" +
                "\"favoriteColor\" : "+request.getColor() +"\n"+
                "}";
        GptRequestDTO.RequstDTO gptRequestDTO= new GptRequestDTO.RequstDTO(gptModel,systemPrompt,userPrompt);
        GptResponseDTO chatGptResponse = restTemplate.postForObject(openAiUrl,gptRequestDTO,GptResponseDTO.class);
        log.info(String.valueOf(fatPercent));
        log.info(String.valueOf(chatGptResponse.getChoices().get(0).getMessage().getContent()));
        String gptResult = String.valueOf(chatGptResponse.getChoices().get(0).getMessage().getContent());
        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(gptResult);
        JSONArray fashionTops=(JSONArray) object.get("fashionTops");
        JSONArray fashionBottoms=(JSONArray) object.get("fashionBottoms");
         JSONArray newFashionTops = new JSONArray();
        if(request.getColor()!=null) {
            String colorName = determineColorFamily(request.getColor());
            JSONObject newFashionTop = new JSONObject();
            newFashionTop.put("color", colorName);
            newFashionTop.put("type", "후드티");
            newFashionTop.put("content", "노멀하고 " + colorName + "인 후드티를 추천합니다.");
            fashionTops.add(0,newFashionTop);
        }


        //네이버 쇼핑 사용
        RestTemplate rest= new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",naverClientId);
        headers.set("X-Naver-Client-Secret",naverClientSecret);
        String body="";

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        String finalGenderName = genderName;
        //현재 네이버 쇼핑 api 호출 에러 이건 추후 아님 틈틈히 할예정
        //상의 추천 반복해서 쇼핑몰 매핑
        List<FasRecResponseDTO.ViewFashionTopDTO> createFashionTopResultDTOS= (List<FasRecResponseDTO.ViewFashionTopDTO>) fashionTops.stream()
                .map(fahsionTop ->{
                    JSONObject fashionTopObject=(JSONObject) fahsionTop;
                    log.info(String.valueOf(headers.get("X-Naver-Client-Id")));
                     ResponseEntity<String> responseEntity =
                        rest.exchange("https://openapi.naver.com/v1/search/shop.json?query="+finalGenderName +" "+fashionTopObject.get("color")+" "+fashionTopObject.get("type"),
                                HttpMethod.GET, requestEntity, String.class);
                    HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
                    int status=httpStatus.value();
                    String response= responseEntity.getBody();
                    try {
                        JSONObject itemObject= (JSONObject) jsonParser.parse(response);
                        JSONArray items= (JSONArray) itemObject.get("items");
                        JSONObject item = (JSONObject) items.get(0);
                        FasRecResponseDTO.NaverItemDTO naverItem = FasRecConverter.toItem(item);
                         return (FasRecResponseDTO.ViewFashionTopDTO) FasRecConverter.toFashionTopDTO(fashionTopObject,naverItem);
                    } catch (ParseException e) {
                        return null;
                    }

                }).collect(Collectors.toList());
        //하의 추천 반복해서 쇼핑몰 매핑

        List<FasRecResponseDTO.ViewFashionBottomDTO> createFashionBottomResultDTOS= (List<FasRecResponseDTO.ViewFashionBottomDTO>) fashionBottoms.stream()
                .map(fahsionBottom ->{
                    JSONObject fashionBottomObject=(JSONObject) fahsionBottom;
                     ResponseEntity<String> responseEntity =
                        rest.exchange("https://openapi.naver.com/v1/search/shop.json?query="+ finalGenderName +" "+fashionBottomObject.get("color")+" "+fashionBottomObject.get("type"),
                                HttpMethod.GET, requestEntity, String.class);
                    HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
                    int status=httpStatus.value();
                    String response= responseEntity.getBody();
                    try {
                        JSONObject itemObject= (JSONObject) jsonParser.parse(response);
                        JSONArray items= (JSONArray) itemObject.get("items");
                        JSONObject item = (JSONObject) items.get(0);
                        FasRecResponseDTO.NaverItemDTO naverItem = FasRecConverter.toItem(item);
                         return FasRecConverter.toFashionBottomDTO(fashionBottomObject,naverItem);
                    } catch (ParseException e) {
                        return null;
                    }

                }).collect(Collectors.toList());

        return FasRecConverter.toCreateResultDTO(gender,type,request.getHeight(),request.getWeight(),createFashionTopResultDTOS,createFashionBottomResultDTOS);
    }
     private Double parseFatFromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);
            JsonNode predictions = root.path("api_data").path("predictions");
            if (!predictions.isMissingNode() && predictions.isArray()) {
                for (JsonNode node : predictions) {
                    if (node.has("fat")) {
                        return node.get("fat").asDouble();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String determineColorFamily(String hexCode) {
        // #을 제거하고 16진수 문자열을 파싱하여 RGB 값을 구함
        int r = Integer.parseInt(hexCode.substring(1, 3), 16);
        int g = Integer.parseInt(hexCode.substring(3, 5), 16);
        int b = Integer.parseInt(hexCode.substring(5, 7), 16);

        // 색상 계열을 결정하는 로직
        if (r > 200 && g < 100 && b < 100) {
            return "빨간색";
        } else if (g > 200 && r < 100 && b < 100) {
            return "초록색";
        } else if (b > 200 && r < 100 && g < 100) {
            return "파란색";
        } else if (r > 200 && g > 200 && b < 100) {
            return "노란색";
        } else if (r > 100 && g < 100 && b > 100) {
            return "보라색";
        } else if (r < 100 && g > 100 && b > 100) {
            return "시안색";
        } else if (r > 150 && g > 150 && b > 150) {
            return "하얀색";
        } else if (r < 100 && g < 100 && b < 100) {
            return "검정색";
        } else {
            return "Other";
        }
    }
}
