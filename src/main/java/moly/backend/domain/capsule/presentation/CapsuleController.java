package moly.backend.domain.capsule.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.presentation.dto.request.CapsuleCreateRequest;
import moly.backend.domain.capsule.presentation.dto.request.CapsuleLocationRequest;
import moly.backend.domain.capsule.presentation.dto.response.CapsuleDetailResponse;
import moly.backend.domain.capsule.presentation.dto.response.CapsuleListResponse;
import moly.backend.domain.capsule.service.CapsuleArrivalService;
import moly.backend.domain.capsule.service.CapsuleCreateService;
import moly.backend.domain.capsule.service.CapsuleDeleteService;
import moly.backend.domain.capsule.service.CapsuleDepartureService;
import moly.backend.domain.capsule.service.CapsuleGetAllService;
import moly.backend.domain.capsule.service.CapsuleGetDetailService;
import moly.backend.domain.capsule.service.CapsuleOpenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/capsules")
@RequiredArgsConstructor
public class CapsuleController {
    private final CapsuleCreateService capsuleCreateService;
    private final CapsuleGetAllService capsuleGetAllService;
    private final CapsuleGetDetailService capsuleGetDetailService;
    private final CapsuleArrivalService capsuleArrivalService;
    private final CapsuleDepartureService capsuleDepartureService;
    private final CapsuleOpenService capsuleOpenService;
    private final CapsuleDeleteService capsuleDeleteService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @AuthenticationPrincipal Long id,
            @Valid @RequestPart(value = "request", required = true) CapsuleCreateRequest request,
            @Size(max = 5, message = "이미지는 최대 5개까지 첨부할 수 있습니다")
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        capsuleCreateService.execute(id, request, images);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CapsuleListResponse getAll(
            @AuthenticationPrincipal Long id,
            @RequestParam("is_opened") boolean isOpened
    ) {
        return capsuleGetAllService.execute(id, isOpened);
    }

    @GetMapping("/{capsule-id}")
    @ResponseStatus(HttpStatus.OK)
    public CapsuleDetailResponse getDetail(
            @AuthenticationPrincipal Long id,
            @PathVariable("capsule-id") @Positive(message = "유효하지 않은 id 값입니다") Long capsuleId
    ) {
        return capsuleGetDetailService.execute(id, capsuleId);
    }

    @PostMapping("/{capsule-id}/arrivals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void arrive(
            @AuthenticationPrincipal Long id,
            @PathVariable("capsule-id") @Positive(message = "유효하지 않은 id 값입니다") Long capsuleId,
            @Valid @RequestBody CapsuleLocationRequest request
    ) {
        capsuleArrivalService.execute(id, capsuleId, request);
    }

    @PostMapping("/{capsule-id}/departures")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depart(
            @AuthenticationPrincipal Long id,
            @PathVariable("capsule-id") @Positive(message = "유효하지 않은 id 값입니다") Long capsuleId,
            @Valid @RequestBody CapsuleLocationRequest request
    ) {
        capsuleDepartureService.execute(id, capsuleId, request);
    }

    @PostMapping("/{capsule-id}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void open(
            @AuthenticationPrincipal Long id,
            @PathVariable("capsule-id") @Positive(message = "유효하지 않은 id 값입니다") Long capsuleId
    ) {
        capsuleOpenService.execute(id, capsuleId);
    }

    @DeleteMapping("/{capsule-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Long id,
            @PathVariable("capsule-id") @Positive(message = "유효하지 않은 id 값입니다") Long capsuleId
    ) {
        capsuleDeleteService.execute(id, capsuleId);
    }
}
