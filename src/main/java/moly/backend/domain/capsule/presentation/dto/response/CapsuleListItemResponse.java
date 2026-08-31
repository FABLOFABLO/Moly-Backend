package moly.backend.domain.capsule.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import moly.backend.domain.capsule.domain.Capsule;
import java.time.LocalDateTime;

public record CapsuleListItemResponse(
        @JsonProperty("capsule_id") Long capsuleId,

        String title,

        String address,

        @JsonProperty("required_time") LocalDateTime requiredTime,

        @JsonProperty("opened_time") LocalDateTime openedTime,

        @JsonProperty("created_at") LocalDateTime createdAt
) {
    public static CapsuleListItemResponse from(Capsule capsule) {
        return new CapsuleListItemResponse(
                capsule.getId(),
                capsule.getTitle(),
                capsule.getAddress(),
                capsule.getRequiredTime(),
                capsule.getOpenedTime(),
                capsule.getCreatedAt()
        );
    }
}
