package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleAlreadyOpenedException extends CustomException {
    public CapsuleAlreadyOpenedException() {
        super(ErrorCode.CAPSULE_ALREADY_OPENED);
    }
}
