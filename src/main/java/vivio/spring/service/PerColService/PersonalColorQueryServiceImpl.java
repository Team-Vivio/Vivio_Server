package vivio.spring.service.PerColService;

import com.amazonaws.services.s3.AmazonS3;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vivio.spring.converter.PerColConverter;
import vivio.spring.domain.*;
import vivio.spring.domain.mapping.ColorRecommendColor;
import vivio.spring.domain.mapping.HairColor;
import vivio.spring.repository.*;
import vivio.spring.web.dto.PerColResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class PersonalColorQueryServiceImpl implements PersonalColorQueryService {
    private final UserRepository userRepository;
    private final PerBeautyRepository perBeautyRepository;
    private final PerColRepository perColRepository;
    private final PerHairReposiotry perHairReposiotry;
    private final PerColRecRepository perColRecRepository;
    private final HairColorRepository hairColorRepository;
    private final BeautyColorRepository beautyColorRepository;
    private final ColorRecommendColorRepository colorRecommendColorRepository;
    private final ColorRepository colorRepository;
    private final AmazonS3 amazonS3;
    @Override
    @Transactional
    public List<PerColResponseDTO.ViewListResultDTO> ViewPerColList(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        List<PersonalColor> personalColors=perColRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PerColResponseDTO.ViewListResultDTO> viewListResultDTO=personalColors.stream()
                .map(personalColor -> {

                    return PerColConverter.toViewListResultDTO(personalColor);
                }).collect(Collectors.toList());
        return viewListResultDTO;
    }
    @Override
    @Transactional
    public PerColResponseDTO.ViewResponseDTO ViewPerCol(Long perColId, Long userId){
        Optional<PersonalColor> personalColorOptional=perColRepository.findById(perColId);
        PersonalColor personalColor=personalColorOptional.get();
        Optional<User> userOptional=userRepository.findById(userId);
        User user=userOptional.get();
        Hair hair=personalColor.getHair();
        Beauty beauty=personalColor.getBeauty();
        ColorRecommend colorRecommend=personalColor.getColorRecommend();

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
        return PerColConverter.toViewResult(personalColor.getUser().getName(),personalColor.getImage(),personalColor.getGender(),personalColor.getSession(),personalColor.getTone(),viewHairDTO,viewBeautyDTO,viewPersonalColorDTO);
    }
}
