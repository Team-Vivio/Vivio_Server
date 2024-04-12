package vivio.spring.apiPayLoad.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vivio.spring.apiPayLoad.code.BaseErrorCode;
import vivio.spring.apiPayLoad.code.ErrorReasonDTO;
@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
