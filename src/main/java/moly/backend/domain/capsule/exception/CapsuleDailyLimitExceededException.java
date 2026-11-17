package moly.backend.domain.capsule.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class CapsuleDailyLimitExceededException extends CustomException {
    public CapsuleDailyLimitExceededException() {
        super(ErrorCode.CAPSULE_DAILY_LIMIT_EXCEEDED);
    }
}
