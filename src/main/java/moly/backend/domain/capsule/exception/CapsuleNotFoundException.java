package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleNotFoundException extends CustomException {
    public CapsuleNotFoundException() {
        super(ErrorCode.CAPSULE_NOT_FOUND);
    }
}
