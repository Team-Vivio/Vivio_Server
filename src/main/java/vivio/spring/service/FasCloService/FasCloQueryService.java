package vivio.spring.service.FasCloService;

import jakarta.transaction.Transactional;
import vivio.spring.web.dto.FasCloResponseDTO;

public interface FasCloQueryService {
    @Transactional
    FasCloResponseDTO.ViewListResultDTO viewList(Long userId);

    @Transactional
    FasCloResponseDTO.ViewItemDTO viewItem(Long userId, Long FashionCloId);
}
