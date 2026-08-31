package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleInvalidInputException extends CustomException {
    public CapsuleInvalidInputException() {
        super(ErrorCode.CAPSULE_INVALID_INPUT);
    }
}
