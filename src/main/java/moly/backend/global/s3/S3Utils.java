package moly.backend.global.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moly.backend.global.s3.exception.S3InvalidFileException;
import moly.backend.global.s3.exception.S3UploadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Utils {

    @Value("${aws.s3.bucket}")
    private String bucket;
    private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );
    private final S3Client s3Client;

    public String upload(MultipartFile file, String directory) {
        validateImageFile(file);

        String fileName = createFileName(file.getOriginalFilename());
        String key = createKey(directory, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException | SdkException exception) {
            log.error("S3 파일 업로드에 실패했습니다. key={}", key, exception);
            throw new S3UploadFailedException();
        }

        return getFileUrl(key);
    }

    public String getFileUrl(String key) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {throw new S3InvalidFileException();}

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_CONTENT_TYPES.contains(contentType)) {
            throw new S3InvalidFileException();
        }
    }

    private String createFileName(String originalFileName) {
        String extension = getExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();

        if (extension.isBlank()) {return uuid;}

        return uuid + "." + extension;
    }

    private String getExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {return "";}

        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex < 0 || extensionIndex == fileName.length() - 1) {return "";}

        return fileName.substring(extensionIndex + 1).toLowerCase();
    }

    private String createKey(String directory, String fileName) {
        String normalizedDirectory = directory == null
                ? "" : directory.strip().replaceAll("^/+|/+$", "");

        if (normalizedDirectory.isBlank()) {return fileName;}

        return normalizedDirectory + "/" + fileName;
    }
}
