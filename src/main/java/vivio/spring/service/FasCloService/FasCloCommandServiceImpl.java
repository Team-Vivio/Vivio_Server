package vivio.spring.service.FasCloService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor

@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class FasCloCommandServiceImpl implements FasCloCommandService{
}
