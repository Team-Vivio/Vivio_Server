package vivio.spring.service.FasCloService;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.web.dto.FasCloRequestDTO;
import vivio.spring.web.dto.FasCloResponseDTO;

import java.io.IOException;
import java.util.List;

public interface FasCloCommandService {


    @Transactional
    FasCloResponseDTO.FasCloJoinDTO joinFasClo(FasCloRequestDTO.JoinRequestDTO request, Long userId);

    @Transactional
    FasCloResponseDTO.CreateDTO createFasClo(FasCloRequestDTO.CrateRequestDTO request, List<MultipartFile> tops, List<MultipartFile> Bottoms, List<MultipartFile> outers) throws IOException;
}
