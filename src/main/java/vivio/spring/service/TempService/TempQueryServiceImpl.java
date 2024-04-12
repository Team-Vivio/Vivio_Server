package vivio.spring.service.TempService;

import vivio.spring.apiPayLoad.code.status.ErrorStatus;
import vivio.spring.apiPayLoad.exception.handler.TempHandler;

public class TempQueryServiceImpl implements TempQueryService {
    @Override
    public void CheckFlag(Integer flag){
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}
