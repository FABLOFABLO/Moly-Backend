package moly.backend.domain.memo.presentation.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemoCreateRequest(
        @NotBlank(message = "내용은 필수입니다")
        @Size(max = 500, message = "내용은 최대 500자입니다")
        String content,

        @NotNull(message = "경도와 위도는 필수입니다")
        @DecimalMin(value = "-90.0", message = "유효하지 않은 위도입니다")
        @DecimalMax(value = "90.0", message = "유효하지 않은 위도입니다")
        Double latitude,

        @NotNull(message = "경도와 위도는 필수입니다")
        @DecimalMin(value = "-180.0", message = "유효하지 않은 경도입니다")
        @DecimalMax(value = "180.0", message = "유효하지 않은 경도입니다")
        Double longitude
) {
}
