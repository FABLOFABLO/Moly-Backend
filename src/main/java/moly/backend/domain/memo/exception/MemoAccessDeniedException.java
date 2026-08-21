package moly.backend.domain.memo.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class MemoAccessDeniedException extends CustomException {

    public MemoAccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
