package moly.backend.global.error;

import moly.backend.global.error.exception.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.message());
    }
}
