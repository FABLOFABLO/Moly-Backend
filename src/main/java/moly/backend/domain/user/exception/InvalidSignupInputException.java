package moly.backend.domain.user.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class InvalidSignupInputException extends CustomException {

    public InvalidSignupInputException() {
        super(ErrorCode.INVALID_SIGNUP_INPUT);
    }
}
