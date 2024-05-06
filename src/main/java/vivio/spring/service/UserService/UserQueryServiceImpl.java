package vivio.spring.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vivio.spring.converter.UserConverter;
import vivio.spring.domain.Bottom;
import vivio.spring.domain.ClothesOuter;
import vivio.spring.domain.Top;
import vivio.spring.domain.User;
import vivio.spring.repository.BottomRepository;
import vivio.spring.repository.OuterRepository;
import vivio.spring.repository.TopRepository;
import vivio.spring.repository.UserRepository;
import vivio.spring.web.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService{
    private final UserRepository userRepository;
    private final TopRepository topRepository;
    private final OuterRepository outerRepository;
    private final BottomRepository bottomRepository;
    @Override
    @Transactional
    public UserResponseDTO.closetBringDTO viewClosets(Long userId, String type){
        Optional<User> userOptional = userRepository.findById(userId);
        List<UserResponseDTO.closetItem> closetItemList=null;
        User user = userOptional.get();
        switch(type){
            case "top":
                List<Top> topList = topRepository.findAllByUser(user);
                closetItemList =topList.stream()
                        .map(top -> {

                            return UserConverter.toClosetItem(top.getId(), top.getImage());
                        }).collect(Collectors.toList());
                break;
            case "outer":
                List<ClothesOuter> clothesOuterList = outerRepository.findAllByUser(user);
                closetItemList = clothesOuterList.stream()
                        .map(clothesOuter -> {
                            return UserConverter.toClosetItem(clothesOuter.getId(), clothesOuter.getImage());
                        }).collect(Collectors.toList());
                break;
            case "bottom":
                List<Bottom> bottomList = bottomRepository.findAllByUser(user);
                closetItemList = bottomList.stream()
                        .map(bottom -> {
                            return UserConverter.toClosetItem(bottom.getId(), bottom.getImage());
                        }).collect(Collectors.toList());
                break;

        }
        return UserConverter.toClosetBringDTO(closetItemList);
    }

    @Override
    @Transactional
    public UserResponseDTO.userinfoDTO viewUserinfo(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        return UserConverter.toUserinfoDTO(user);

    }
}
