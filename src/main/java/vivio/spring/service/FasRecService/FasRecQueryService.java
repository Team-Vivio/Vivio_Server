package vivio.spring.service.FasRecService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.domain.mapping.FashionTop;
import vivio.spring.web.dto.FasRecResponseDTO;

import java.util.List;


public interface FasRecQueryService {
    @Transactional
    List<FasRecResponseDTO.ViewListResultDTO> ViewFasRecList(Long userId);


}
