package moly.backend.domain.capsule.presentation.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CapsuleLocationRequest(
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
