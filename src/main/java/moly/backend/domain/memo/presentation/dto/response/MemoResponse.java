package moly.backend.domain.memo.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import moly.backend.domain.memo.domain.Memo;
import java.time.LocalDateTime;

public record MemoResponse(
        @JsonProperty("memo_id")
        Long memoId,
        String content,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        Double latitude,
        Double longitude
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
                memo.getId(),
                memo.getContent(),
                memo.getCreatedAt(),
                memo.getLocation().getY(),
                memo.getLocation().getX()
        );
    }
}
