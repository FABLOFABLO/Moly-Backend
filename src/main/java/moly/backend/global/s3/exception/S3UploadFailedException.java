package moly.backend.global.s3.exception;

import moly.backend.global.error.exception.CustomException;
import moly.backend.global.error.exception.ErrorCode;

public class S3UploadFailedException extends CustomException {

    public S3UploadFailedException() {
        super(ErrorCode.S3_UPLOAD_FAILED);
    }
}
