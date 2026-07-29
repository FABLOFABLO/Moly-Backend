package moly.backend.global.error;

import lombok.extern.slf4j.Slf4j;
import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequest(
            HttpMessageNotReadableException exception
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        ErrorCode errorCode = ErrorCode.USER_ALREADY_EXISTS;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ErrorResponse> handleRedisConnectionFailure(
            RedisConnectionFailureException exception
    ) {
        log.error("Redis 연결에 실패했습니다.", exception);
        ErrorCode errorCode = ErrorCode.TOKEN_STORE_UNAVAILABLE;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        log.error("처리되지 않은 예외가 발생했습니다.", exception);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }
}
