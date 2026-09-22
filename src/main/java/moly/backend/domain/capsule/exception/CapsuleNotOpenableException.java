package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleNotOpenableException extends CustomException {
    public CapsuleNotOpenableException() {
        super(ErrorCode.CAPSULE_NOT_OPENABLE);
    }
}
