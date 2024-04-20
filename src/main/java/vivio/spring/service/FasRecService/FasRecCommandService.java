package vivio.spring.service.FasRecService;

import jakarta.transaction.Transactional;
import org.json.simple.parser.ParseException;
import vivio.spring.domain.FashionRecommand;
import vivio.spring.web.dto.FasRecRequestDTO;
import vivio.spring.web.dto.FasRecResponseDTO;

import javax.mail.MessagingException;
import java.io.IOException;

public interface FasRecCommandService {
    @Transactional
    FashionRecommand JoinFasRec(long userId, FasRecRequestDTO.JoinDTO requset);

    @Transactional
    FasRecResponseDTO.CreateResultDTO CreateFasRec(FasRecRequestDTO.FasRecCreateDTO requst, String image) throws MessagingException, IOException, ParseException;
}
