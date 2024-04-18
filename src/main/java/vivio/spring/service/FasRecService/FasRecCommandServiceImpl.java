package vivio.spring.service.FasRecService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vivio.spring.converter.FasRecConverter;
import vivio.spring.converter.UserConverter;
import vivio.spring.domain.*;
import vivio.spring.domain.mapping.FashionBottom;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.repository.*;
import vivio.spring.web.dto.FasRecRequestDTO;
import vivio.spring.web.dto.UserRequestDTO;

import java.util.List;
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


}
