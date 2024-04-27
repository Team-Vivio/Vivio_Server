package vivio.spring.service.PerColService;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.web.dto.PerColRequestDTO;
import vivio.spring.web.dto.PerColResponseDTO;

import java.io.IOException;
import java.util.List;

public interface PersonalColorCommandService {
    @Transactional
    PerColResponseDTO.CreateResponseDTO CreatePerCol(PerColRequestDTO.CreateDto requst, MultipartFile file);



    @Transactional
    PerColResponseDTO.JoinResponseDTO JoinPerCol(PerColRequestDTO.PerColJoinDTO request, Long userId, MultipartFile file) throws IOException;



}
