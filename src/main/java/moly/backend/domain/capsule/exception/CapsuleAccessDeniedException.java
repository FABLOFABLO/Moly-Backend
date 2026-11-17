package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleAccessDeniedException extends CustomException {
    public CapsuleAccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
