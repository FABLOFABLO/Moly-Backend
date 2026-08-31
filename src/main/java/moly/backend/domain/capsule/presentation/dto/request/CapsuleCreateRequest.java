package moly.backend.domain.capsule.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;
import java.time.LocalDateTime;
import java.util.List;

public record CapsuleCreateRequest(
        @JsonProperty("expedition_id")
        Long expeditionId,

        @JsonProperty("members_id")
        @UniqueElements(message = "중복된 멤버 id를 입력할 수 없습니다")
        List<@Positive(message = "유효하지 않은 멤버 id입니다") Long> memberIds,

        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 30, message = "제목은 최대 30자입니다")
        String title,

        @NotBlank(message = "내용은 필수입니다")
        @Size(max = 1000, message = "내용은 최대 1000자입니다")
        String content,

        @JsonProperty("required_count")
        @NotNull(message = "개봉 요구 인원은 필수입니다")
        @Positive(message = "개봉 인원은 최소 1명입니다")
        Integer requiredCount,

        @JsonProperty("required_time")
        @NotNull(message = "개봉 일시는 필수입니다")
        LocalDateTime requiredTime,

        @NotNull(message = "경도와 위도는 필수입니다")
        @DecimalMin(value = "-90.0", message = "유효하지 않은 위도입니다")
        @DecimalMax(value = "90.0", message = "유효하지 않은 위도입니다")
        Double latitude,

        @NotNull(message = "경도와 위도는 필수입니다")
        @DecimalMin(value = "-180.0", message = "유효하지 않은 경도입니다")
        @DecimalMax(value = "180.0", message = "유효하지 않은 경도입니다")
        Double longitude
) {
    @JsonIgnore
    @AssertTrue(message = "탐험대와 초대 멤버를 동시에 지정할 수 없습니다")
    public boolean isTargetValid() {
        return expeditionId == null || memberIds == null || memberIds.isEmpty();
    }
}
