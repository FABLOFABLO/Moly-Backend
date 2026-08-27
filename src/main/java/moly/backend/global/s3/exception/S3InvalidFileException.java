package moly.backend.global.s3.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class S3InvalidFileException extends CustomException {

    public S3InvalidFileException() {
        super(ErrorCode.S3_INVALID_FILE);
    }
}
