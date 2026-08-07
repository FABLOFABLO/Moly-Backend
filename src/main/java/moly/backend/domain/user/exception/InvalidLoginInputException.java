package moly.backend.domain.user.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class InvalidLoginInputException extends CustomException {

    public InvalidLoginInputException() {
        super(ErrorCode.INVALID_LOGIN_INPUT);
    }
}
