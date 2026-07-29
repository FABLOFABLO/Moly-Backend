package moly.backend.domain.user.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class EmailTooLongException extends CustomException {

    public EmailTooLongException() {
        super(ErrorCode.EMAIL_TOO_LONG);
    }
}
