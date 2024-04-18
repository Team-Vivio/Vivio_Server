package vivio.spring.service.FasRecService;

import jakarta.transaction.Transactional;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.web.dto.FasRecRequestDTO;

public interface FasRecCommandService {
    @Transactional
    FashionRecommand JoinFasRec(long userId, FasRecRequestDTO.JoinDTO requset);
}
