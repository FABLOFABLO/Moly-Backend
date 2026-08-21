package moly.backend.domain.memo.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class MemoNotFoundException extends CustomException {

    public MemoNotFoundException() {
        super(ErrorCode.MEMO_NOT_FOUND);
    }
}
