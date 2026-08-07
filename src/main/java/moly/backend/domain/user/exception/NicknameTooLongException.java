package moly.backend.domain.user.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class NicknameTooLongException extends CustomException {

    public NicknameTooLongException() {
        super(ErrorCode.NICKNAME_TOO_LONG);
    }
}
