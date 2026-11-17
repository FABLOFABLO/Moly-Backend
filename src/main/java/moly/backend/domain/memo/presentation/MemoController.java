package moly.backend.domain.memo.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import moly.backend.domain.memo.presentation.dto.request.MemoCreateRequest;
import moly.backend.domain.memo.presentation.dto.request.MemoUpdateRequest;
import moly.backend.domain.memo.presentation.dto.response.MemoListResponse;
import moly.backend.domain.memo.presentation.dto.response.MemoResponse;
import moly.backend.domain.memo.service.MemoCreateService;
import moly.backend.domain.memo.service.MemoDeleteService;
import moly.backend.domain.memo.service.MemoGetAllService;
import moly.backend.domain.memo.service.MemoGetDetailService;
import moly.backend.domain.memo.service.MemoUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {
    private final MemoCreateService memoCreateService;
    private final MemoGetAllService memoGetAllService;
    private final MemoGetDetailService memoGetDetailService;
    private final MemoUpdateService memoUpdateService;
    private final MemoDeleteService memoDeleteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @AuthenticationPrincipal Long id,
            @Valid @RequestBody MemoCreateRequest request
    ) {
        memoCreateService.execute(id, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MemoListResponse getAll(@AuthenticationPrincipal Long id) {
        return memoGetAllService.execute(id);
    }

    @GetMapping("/{memo-id}")
    @ResponseStatus(HttpStatus.OK)
    public MemoResponse getDetail(
            @AuthenticationPrincipal Long id,
            @PathVariable("memo-id") @Positive(message = "유효하지 않은 id 값입니다") Long memoId
    ) {
        return memoGetDetailService.execute(id, memoId);
    }

    @PatchMapping("/{memo-id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(
            @AuthenticationPrincipal Long id,
            @PathVariable("memo-id") @Positive(message = "유효하지 않은 id 값입니다") Long memoId,
            @Valid @RequestBody MemoUpdateRequest request
    ) {
        memoUpdateService.execute(id, memoId, request);
    }

    @DeleteMapping("/{memo-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Long id,
            @PathVariable("memo-id") @Positive(message = "유효하지 않은 id 값입니다") Long memoId
    ) {
        memoDeleteService.execute(id, memoId);
    }
}
