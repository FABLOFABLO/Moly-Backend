package moly.backend.domain.memo.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemoUpdateRequest(
        @NotBlank(message = "내용은 필수입니다")
        @Size(max = 500, message = "내용은 최대 500자입니다")
        String content
) {
}
