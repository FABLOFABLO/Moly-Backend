package moly.backend.domain.memo.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import moly.backend.domain.memo.domain.Memo;
import java.util.List;

public record MemoListResponse(
        @JsonProperty("memo_count")
        Long memoCount,
        List<MemoResponse> memos
) {
    public static MemoListResponse of(Long memoCount, List<Memo> memos) {
        List<MemoResponse> responses = memos.stream()
                .map(MemoResponse::from)
                .toList();

        return new MemoListResponse(memoCount, responses);
    }
}
