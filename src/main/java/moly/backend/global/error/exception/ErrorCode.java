package moly.backend.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다."),
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    INVALID_SIGNUP_INPUT(HttpStatus.BAD_REQUEST, "email, password, nickname은 필수입니다."),
    INVALID_LOGIN_INPUT(HttpStatus.BAD_REQUEST, "닉네임 와 password는 필수입니다."),
    EMAIL_TOO_LONG(HttpStatus.BAD_REQUEST, "email은 128자 이하여야 합니다."),
    PASSWORD_TOO_LONG(HttpStatus.BAD_REQUEST, "password는 255자 이하여야 합니다."),
    NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "닉네임은 30자 이하여야 합니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 정보입니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "닉네임 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 토큰입니다."),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    S3_INVALID_FILE(HttpStatus.BAD_REQUEST, "업로드할 수 없는 파일입니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    CAPSULE_INVALID_INPUT(HttpStatus.BAD_REQUEST, "타임캡슐 요청 값이 올바르지 않습니다."),
    CAPSULE_INVALID_TIME(HttpStatus.BAD_REQUEST, "개봉 일시는 현재로부터 7일 이후여야 합니다."),
    CAPSULE_NOT_OPENABLE(HttpStatus.BAD_REQUEST, "타임캡슐을 개봉할 수 없는 상태입니다."),
    CAPSULE_DAILY_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "하루에 생성할 수 있는 타임캡슐 수를 초과했습니다."),
    CAPSULE_ALREADY_OPENED(HttpStatus.CONFLICT, "이미 개봉된 타임캡슐입니다."),
    CAPSULE_INVALID_ARRIVAL_STATUS(HttpStatus.CONFLICT, "현재 도착 상태에서 처리할 수 없는 요청입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 사용자입니다."),
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 메모입니다."),
    CAPSULE_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 타임캡슐입니다."),

    KAKAO_ADDRESS_LOOKUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "주소 정보를 조회할 수 없습니다."),

    TOKEN_STORE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "토큰 저장소에 연결할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
