package vivio.spring.apiPayLoad.exception.handler;

import vivio.spring.apiPayLoad.code.BaseErrorCode;
import vivio.spring.apiPayLoad.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
