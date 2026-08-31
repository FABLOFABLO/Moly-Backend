package moly.backend.domain.capsule.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import moly.backend.domain.capsule.domain.Capsule;
import java.time.LocalDateTime;
import java.util.List;

public record CapsuleDetailResponse(
        @JsonProperty("capsule_id") Long capsuleId,

        @JsonProperty("creator_nickname") String creatorNickname,

        @JsonProperty("expedition_id") Long expeditionId,

        String title,

        String content,

        List<String> images,

        Double latitude,

        Double longitude,

        String address,

        @JsonProperty("road_address") String roadAddress,

        @JsonProperty("required_count") int requiredCount,

        @JsonProperty("required_time") LocalDateTime requiredTime,

        @JsonProperty("opened_time") LocalDateTime openedTime,

        @JsonProperty("created_at") LocalDateTime createdAt
) {
    public static CapsuleDetailResponse from(Capsule capsule, List<String> images) {
        boolean isOpened = capsule.getOpenedTime() != null;
        return new CapsuleDetailResponse(
                capsule.getId(),
                capsule.getUser().getNickname(),
                capsule.getExpedition() == null ? null : capsule.getExpedition().getId(),
                capsule.getTitle(),
                isOpened ? capsule.getContent() : null,
                isOpened ? images : List.of(),
                capsule.getLocation().getY(),
                capsule.getLocation().getX(),
                capsule.getAddress(),
                capsule.getRoadAddress(),
                capsule.getRequiredCount(),
                capsule.getRequiredTime(),
                capsule.getOpenedTime(),
                capsule.getCreatedAt()
        );
    }
}
