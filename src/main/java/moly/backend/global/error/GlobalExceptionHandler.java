package moly.backend.global.error;

import lombok.extern.slf4j.Slf4j;
import moly.backend.domain.user.presentation.dto.request.UserLoginRequest;
import moly.backend.domain.user.presentation.dto.request.UserSignupRequest;
import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(
            MethodArgumentNotValidException exception
    ) {
        ErrorCode errorCode = resolveValidationErrorCode(exception);
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError == null
                ? errorCode.message()
                : fieldError.getDefaultMessage();

        return ResponseEntity
                .status(errorCode.status())
                .body(new ErrorResponse(errorCode.name(), message));
    }

    private ErrorCode resolveValidationErrorCode(MethodArgumentNotValidException exception) {
        Class<?> requestType = exception.getParameter().getParameterType();

        if (requestType == UserLoginRequest.class) {
            return ErrorCode.INVALID_LOGIN_INPUT;
        }

        if (requestType == UserSignupRequest.class) {
            boolean hasBlankField = exception.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getCode)
                    .anyMatch("NotBlank"::equals);

            if (hasBlankField) {
                return ErrorCode.INVALID_SIGNUP_INPUT;
            }

            FieldError fieldError = exception.getBindingResult().getFieldErrors().stream()
                    .filter(error -> "Size".equals(error.getCode()))
                    .findFirst()
                    .orElse(null);

            if (fieldError != null) {
                return switch (fieldError.getField()) {
                    case "email" -> ErrorCode.EMAIL_TOO_LONG;
                    case "password" -> ErrorCode.PASSWORD_TOO_LONG;
                    case "nickname" -> ErrorCode.NICKNAME_TOO_LONG;
                    default -> ErrorCode.INVALID_SIGNUP_INPUT;
                };
            }

            return ErrorCode.INVALID_SIGNUP_INPUT;
        }

        return ErrorCode.INVALID_REQUEST_VALUE;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(
            HandlerMethodValidationException exception
    ) {
        String message = exception.getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse(ErrorCode.INVALID_REQUEST_VALUE.message());

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ErrorCode.INVALID_REQUEST_VALUE.name(), message));
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPart(
            MissingServletRequestPartException exception
    ) {
        ErrorCode errorCode = ErrorCode.S3_INVALID_FILE;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_VALUE;

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        String message = exception.getMostSpecificCause().getMessage();
        ErrorCode errorCode = message != null && message.contains("uk_capsule_")
                ? ErrorCode.CAPSULE_INVALID_INPUT
                : ErrorCode.USER_ALREADY_EXISTS;

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
