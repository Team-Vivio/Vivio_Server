package vivio.spring.service.SeaCloService;

import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vivio.spring.web.dto.SeaCloReqeustDTO;
import vivio.spring.web.dto.SeaCloResponseDTO;

import java.io.File;
import java.io.IOException;

public interface SeaCloCommandService {
    @Transactional()
    SeaCloResponseDTO.SeaCloListDTO createSeaClo(SeaCloReqeustDTO.createDTO request, MultipartFile file) throws IOException;
}
