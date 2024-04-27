package vivio.spring.service.PerColService;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.web.dto.PerColRequestDTO;
import vivio.spring.web.dto.PerColResponseDTO;

public interface PersonalColorCommandService {
    @Transactional
    PerColResponseDTO.CreateResponseDTO CreatePerCol(PerColRequestDTO.CreateDto requst, MultipartFile file);
}
