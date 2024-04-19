package vivio.spring.service.FasRecService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vivio.spring.converter.FasRecConverter;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.User;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.repository.*;
import vivio.spring.web.dto.FasRecResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasRecQueryServiceImpl implements FasRecQueryService {
    private final FasRecRepository fasRecRepository;
    private final UserRepository userRepository;
    private  final FasTopRepository fasTopRepository;
    private final FasBottomRepository fasBottomRepository;
    private final FasColorRepository fasColorRepository;
    private final FasTypeRepository fasTypeRepository;
    private FasRecConverter fasRecConverter;
    @Override
    public List<FasRecResponseDTO.ViewListResultDTO> ViewFasRecList(Long userId){

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);  // 혹은 적절한 예외 처리
        }
        User user = userOptional.get();
        List<FashionRecommand> fashionRecommandList = fasRecRepository.findAllByUser(user);

        List<FasRecResponseDTO.ViewListResultDTO> fashionList=  fashionRecommandList.stream()
                .map(fashionRecommand -> {
                    Optional<FashionTop> fashionTop = fasTopRepository.findFirstByFashionRecommandId(fashionRecommand.getId());
                    Optional<FashionBottom> fashionBottom = fasBottomRepository.findFirstByFashionRecommandId(fashionRecommand.getId());
                    if(fashionTop.isPresent()) {

                        FashionTop newFashionTop=fashionTop.get();
                        String link=newFashionTop.getLink();
                        String image =newFashionTop.getImage();
                        String type = fashionRecommand.getType().name();
                        return FasRecConverter.toViewListResultDTO(fashionRecommand,image,link,type);
                    }else {
                        FashionBottom newFashionBottom=fashionBottom.get();
                        String link = newFashionBottom.getLink();
                        String image= newFashionBottom.getImage();
                        String type = fashionRecommand.getType().name();
                        return FasRecConverter.toViewListResultDTO(fashionRecommand,image,link,type);
                    }
                        }
                ).collect(Collectors.toList());


        return fashionList;
    }

}
