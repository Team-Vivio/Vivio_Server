package vivio.spring.service.PerColService;

import jakarta.transaction.Transactional;
import vivio.spring.web.dto.PerColResponseDTO;

import java.util.List;

public interface PersonalColorQueryService {
    @Transactional
    List<PerColResponseDTO.ViewListResultDTO> ViewPerColList(Long userId);

    @Transactional
    PerColResponseDTO.ViewResponseDTO ViewPerCol(Long perColId, Long userId);
}
