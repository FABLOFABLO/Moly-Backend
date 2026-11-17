package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleInvalidTimeException extends CustomException {
    public CapsuleInvalidTimeException() {
        super(ErrorCode.CAPSULE_INVALID_TIME);
    }
}
