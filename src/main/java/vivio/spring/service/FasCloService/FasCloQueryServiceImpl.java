package vivio.spring.service.FasCloService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vivio.spring.converter.FasCloConverter;
import vivio.spring.domain.FashionCloset;
import vivio.spring.domain.User;
import vivio.spring.domain.mapping.FashionClosetFashionStyle;
import vivio.spring.repository.FasCloFasStyRepository;
import vivio.spring.repository.FasCloRepository;
import vivio.spring.repository.FasStyRepository;
import vivio.spring.repository.UserRepository;
import vivio.spring.web.dto.FasCloResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasCloQueryServiceImpl implements FasCloQueryService {
    private final FasCloRepository fasCloRepository;
    private final FasCloFasStyRepository fasCloFasStyRepository;
    private final FasStyRepository fasStyRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public FasCloResponseDTO.ViewListResultDTO viewList(Long userId){
        Optional<User> userOptional= userRepository.findById(userId);
        User user=userOptional.get();
        List<FashionCloset> fashionClosets= fasCloRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<FasCloResponseDTO.ViewListItemDTO> items= fashionClosets.stream().map(
                fashionCloset -> {
                        Optional<FashionClosetFashionStyle>fashionClosetFashionStyleOptional= fasCloFasStyRepository.findFirstByFashionCloset(fashionCloset);
                        FashionClosetFashionStyle fashionClosetFashionStyle= fashionClosetFashionStyleOptional.get();
                        return FasCloConverter.toViewListItem(fashionClosetFashionStyle);

                }
        ).collect(Collectors.toList());
        return FasCloConverter.toViewList(items);
    }
    @Override
    @Transactional
    public FasCloResponseDTO.ViewItemDTO viewItem(Long userId,Long fashionCloId){
        Optional<User> userOptional= userRepository.findById(userId);
        User user=userOptional.get();
        Optional<FashionCloset> fashionClosetOptional=fasCloRepository.findById(fashionCloId);
        FashionCloset fashionCloset= fashionClosetOptional.get();
        List<FashionClosetFashionStyle> fashionClosetFashionStyleList=fasCloFasStyRepository.findAllByFashionCloset(fashionCloset);
        List<FasCloResponseDTO.ItemDTO> fashionItems=fashionClosetFashionStyleList.stream()
                .map(FasCloConverter::toItemDTO).collect(Collectors.toList());

        return FasCloConverter.toViewItemDTO(fashionCloset,fashionItems);
    }
}
