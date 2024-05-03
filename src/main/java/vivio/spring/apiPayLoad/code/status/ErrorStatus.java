package vivio.spring.apiPayLoad.code.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import vivio.spring.apiPayLoad.code.BaseErrorCode;
import vivio.spring.apiPayLoad.code.ErrorReasonDTO;
@Getter
@AllArgsConstructor

public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    //이메일 고나련 응답
    EMAIL_INVAILD(HttpStatus.NOT_FOUND,"EMAIL4001","이메일이 존재하지 않습니다"),
    EMAIL_SOCIAL(HttpStatus.BAD_REQUEST,"EMAIL4002","소셜로그인 계정입니다"),

    // 멤버 관련 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    // ~~~ 관련 응답 ....
    //For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST,"TEMP4001","이거는 테스트"),

    EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "USER4001","인증번호가 일치 하지 않습니다."),
    USER_EXCEPTION(HttpStatus.BAD_REQUEST, "USER4002","비밀번호가 일치 하지 않습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
