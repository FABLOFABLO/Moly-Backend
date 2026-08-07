package moly.backend.domain.user.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class PasswordTooLongException extends CustomException {

    public PasswordTooLongException() {
        super(ErrorCode.PASSWORD_TOO_LONG);
    }
}
