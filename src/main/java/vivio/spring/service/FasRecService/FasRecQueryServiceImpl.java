package vivio.spring.service.FasRecService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
        List<FashionRecommand> fashionRecommandList = fasRecRepository.findAllByUser(user,Sort.by(Sort.Direction.DESC, "createdAt"));

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
    @Override
    public FashionRecommand ViewFasRec(Long id){
        Optional<FashionRecommand> fashionRecommandOptional =fasRecRepository.findById(id);
        if(fashionRecommandOptional.isPresent()) {
            FashionRecommand fashionRecommand = fashionRecommandOptional.get();
            return fashionRecommand;
        }else{
            return null;
        }

    }
    @Override
    public FasRecResponseDTO.ViewResultDTO ViewFasRecResult(FashionRecommand fashionRecommand){

       List<FashionTop> fashionTops=fasTopRepository.findAllByFashionRecommandId(fashionRecommand.getId());
       List<FashionBottom> fashionBottoms=fasBottomRepository.findAllByFashionRecommandId(fashionRecommand.getId());
       List<FasRecResponseDTO.ViewFashionTopDTO> fashionTopDTOS=fashionTops.stream()
               .map(fashionTop -> {
                   String type = fashionTop.getType().getName();
                   String color = fashionTop.getColor().getName();
                   return FasRecConverter.toViewFashionTopDTO(fashionTop,type,color);

                    }

               )
               .collect(Collectors.toList());
       List<FasRecResponseDTO.ViewFashionBottomDTO> fashionBottomDTOS=fashionBottoms.stream()
               .map(fashionBottom -> {
                   String type = fashionBottom.getType().getName();
                   String color = fashionBottom.getColor().getName();
                   return FasRecConverter.toViewFashionBottomDTO(fashionBottom,type,color);

                   }
               )
               .collect(Collectors.toList());
       return FasRecConverter.toViewResultDTO(fashionRecommand,fashionBottomDTOS,fashionTopDTOS);


    }

}
