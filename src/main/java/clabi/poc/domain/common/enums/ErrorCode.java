package clabi.poc.domain.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNAUTHORIZED(1000, ResultMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED),
    INVALID_ARGUMENT(1001, ResultMessage.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND(1002, ResultMessage.ENTITY_NOT_FOUND, HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(1003, ResultMessage.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),

    // authenticate
    NOT_VALID_MEMBER_INFO(2000, ResultMessage.NOT_VALID_MEMBER_INFO, HttpStatus.BAD_REQUEST),
    MEMBER_NOT_EXIST(2001, ResultMessage.MEMBER_NOT_EXIST, HttpStatus.NOT_FOUND),

    // jwt
    JWT_NOT_FIND_TOKEN(3000, ResultMessage.JWT_NOT_FIND_TOKEN, HttpStatus.UNAUTHORIZED),
    JWT_TOKEN_PARSING(3001, ResultMessage.JWT_TOKEN_PARSING, HttpStatus.BAD_REQUEST),
    JWT_ACCESS_TOKEN_EXPIRED(3002, ResultMessage.JWT_ACCESS_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED),
    JWT_REFRESH_TOKEN_EXPIRED(3003, ResultMessage.JWT_REFRESH_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED),
    JWT_UNKNOWN_ERROR(3004, ResultMessage.JWT_UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
    JWT_INVALID_ACCESS_TOKEN(3005, ResultMessage.JWT_INVALID_ACCESS_TOKEN, HttpStatus.BAD_REQUEST),
    JWT_INVALID_REFRESH_TOKEN(3006, ResultMessage.JWT_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST),
    EXIST_BLACK_LIST(3007, ResultMessage.EXIST_BLACK_LIST, HttpStatus.UNAUTHORIZED),

    // user
    NOT_FOUND_USER(4000, ResultMessage.NOT_FOUND_USER, HttpStatus.NOT_FOUND),

    // param error
    PARAM_NOT_VALID(5000, ResultMessage.PARAM_NOT_VALID, HttpStatus.BAD_REQUEST),

    // stats
    STATS_NOT_FOUND(6000, ResultMessage.STATS_NOT_FOUND, HttpStatus.NOT_FOUND),
    STATS_ALREADY_EXIST(6001, ResultMessage.STATS_ALREADY_EXIST, HttpStatus.CONFLICT),

    // chat
    CHAT_MEMO_ALREADY_EXIST(7000, ResultMessage.CHAT_MEMO_ALREADY_EXIST, HttpStatus.CONFLICT),
    SATISTACTION_ALREADY_EXIST(7001, ResultMessage.SATISTACTION_ALREADY_EXIST, HttpStatus.CONFLICT),
            ;

    private final Integer errorCode;
    private final String resultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(Integer errorCode, String resultMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.resultMessage = resultMessage;
        this.httpStatus = httpStatus;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public HttpStatus getResultHttpStatus() {return httpStatus;}


    public interface ResultMessage {
        String UNAUTHORIZED = "인증에 실패하였습니다.";
        String INVALID_ARGUMENT = "잘못된 파라미터입니다.";
        String ENTITY_NOT_FOUND = "존재하지 않는 테이블입니다.";
        String NOT_VALID_MEMBER_INFO = "잘못된 아이디, 비밀번호 입니다.";
        String MEMBER_NOT_EXIST = "존재하지 않는 사용자 입니다.";
        String PARAM_NOT_VALID = "파라미터 오류입니다.";
        String INTERNAL_ERROR = "시스템 오류가 발생하였습니다. 다시 시도해주세요.";
        String JWT_INVALID_ACCESS_TOKEN = "잘못된 ACCESS TOKEN 입니다.";
        String JWT_INVALID_REFRESH_TOKEN = "잘못된 REFRESH TOKEN 입니다.";
        // 사용자 에러
        String NOT_FOUND_USER = "사용자 정보가 존재하지 않습니다.";
        // JWT
        String JWT_NOT_FIND_TOKEN = "토큰 정보가 누락되어있습니다.";
        String JWT_TOKEN_PARSING = "토큰 파싱 에러가 발생하였습니다.";
        String JWT_ACCESS_TOKEN_EXPIRED = "Access Token 이 만료되었습니다.";
        String JWT_REFRESH_TOKEN_EXPIRED = "Refresh Token 이 만료되었습니다. 다시 로그인해주세요.";
        String JWT_UNKNOWN_ERROR = "JWT 에러가 발생하였습니다.";
        String EXIST_BLACK_LIST = "블랙리스트(로그아웃) 된 토큰입니다. 다시 로그인해주세요.";

        String STATS_NOT_FOUND = "해당 날짜에 통계 정보가 존재하지 않습니다.";
        String STATS_ALREADY_EXIST = "이미 등록된 통계 정보 입니다.";
        
        String CHAT_MEMO_ALREADY_EXIST = "채팅 메모입니다.";
        String SATISTACTION_ALREADY_EXIST = "이미 등록된 만족도 입니다.";
    }
}
