package vivio.spring.apiPayLoad.exception.handler;

import vivio.spring.apiPayLoad.code.BaseErrorCode;
import vivio.spring.apiPayLoad.exception.GeneralException;
import vivio.spring.converter.UserConverter;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
