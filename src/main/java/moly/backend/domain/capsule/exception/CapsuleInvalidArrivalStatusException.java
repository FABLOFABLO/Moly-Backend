package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleInvalidArrivalStatusException extends CustomException {
    public CapsuleInvalidArrivalStatusException() {
        super(ErrorCode.CAPSULE_INVALID_ARRIVAL_STATUS);
    }
}
