package vivio.spring.service.FasRecService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.User;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.repository.FasRecRepository;
import vivio.spring.repository.UserRepository;
import vivio.spring.web.dto.FasRecRequestDTO;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasRecQueryServiceImpl implements FasRecQueryService {
    private final FasRecRepository fasRecRepository;
    private final UserRepository userRepository;
    @Override
    public List<FashionRecommand> ViewFasRecList(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);  // 혹은 적절한 예외 처리
        }
        User user = userOptional.get();
        List<FashionRecommand> fashionRecommandList = fasRecRepository.findAllByUser(user);
        return fashionRecommandList;
    }
    @Override
    public List<FashionTop> fashionTopList(Long fasRecId){
        Optional<FashionRecommand> fashionRecommandOptional = fasRecRepository.findById(fasRecId);
        if(fashionRecommandOptional.isEmpty()){
            return null;
        }
        FashionRecommand fashionRecommand= fashionRecommandOptional.get();
        //컬러 가지고 옥
    }
}
