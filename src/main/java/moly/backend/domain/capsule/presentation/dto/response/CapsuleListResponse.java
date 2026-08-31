package moly.backend.domain.capsule.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CapsuleListResponse(
        @JsonProperty("capsules") List<CapsuleListItemResponse> capsules
) {
}
