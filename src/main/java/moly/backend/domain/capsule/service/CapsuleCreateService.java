package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.CapsuleArrival;
import moly.backend.domain.capsule.domain.CapsuleImage;
import moly.backend.domain.capsule.domain.CapsuleMember;
import moly.backend.domain.capsule.domain.repository.CapsuleArrivalRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleImageRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleMemberRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.exception.CapsuleDailyLimitExceededException;
import moly.backend.domain.capsule.exception.CapsuleInvalidInputException;
import moly.backend.domain.capsule.exception.CapsuleInvalidTimeException;
import moly.backend.domain.capsule.presentation.dto.request.CapsuleCreateRequest;
import moly.backend.domain.expedition.domain.Expedition;
import moly.backend.domain.expedition.domain.UserExpedition;
import moly.backend.domain.expedition.domain.repository.ExpeditionRepository;
import moly.backend.domain.expedition.domain.repository.UserExpeditionRepository;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import moly.backend.global.kakao.KakaoAddressClient;
import moly.backend.global.s3.S3Utils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CapsuleCreateService {
    private static final int WGS84_SRID = 4326;
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), WGS84_SRID);

    private final CapsuleRepository capsuleRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleArrivalRepository capsuleArrivalRepository;
    private final CapsuleImageRepository capsuleImageRepository;
    private final UserRepository userRepository;
    private final ExpeditionRepository expeditionRepository;
    private final UserExpeditionRepository userExpeditionRepository;
    private final KakaoAddressClient kakaoAddressClient;
    private final S3Utils s3Utils;

    @Transactional
    public void execute(
            Long userId,
            CapsuleCreateRequest request,
            List<MultipartFile> images
    ) {
        User creator = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Expedition expedition = request.expeditionId() == null
                ? null
                : expeditionRepository.findByIdForUpdate(request.expeditionId())
                        .orElseThrow(CapsuleInvalidInputException::new);
        List<User> members = resolveMembers(creator, expedition, request.memberIds());
        if (request.requiredCount() > members.size()) {
            throw new CapsuleInvalidInputException();
        }
        if (request.requiredTime().isBefore(LocalDateTime.now(KOREA_ZONE_ID).plusDays(7))) {
            throw new CapsuleInvalidTimeException();
        }
        validateDailyLimit(expedition, members);

        KakaoAddressClient.KakaoAddress address = kakaoAddressClient.findAddress(
                request.longitude(), request.latitude()
        );
        Point location = GEOMETRY_FACTORY.createPoint(
                new Coordinate(request.longitude(), request.latitude())
        );
        location.setSRID(WGS84_SRID);
        Capsule capsule = capsuleRepository.save(Capsule.builder()
                .user(creator)
                .expedition(expedition)
                .title(request.title())
                .content(request.content())
                .location(location)
                .requiredCount(request.requiredCount())
                .requiredTime(request.requiredTime())
                .address(address.address())
                .roadAddress(address.roadAddress())
                .build());

        List<CapsuleMember> capsuleMembers = members.stream()
                .map(member -> CapsuleMember.builder()
                        .capsule(capsule)
                        .user(member)
                        .build())
                .toList();
        capsuleMemberRepository.saveAll(capsuleMembers);
        capsuleArrivalRepository.saveAll(capsuleMembers.stream()
                .map(member -> CapsuleArrival.builder()
                        .capsuleMember(member)
                        .build())
                .toList());

        saveImages(capsule, images);
    }

    private List<User> resolveMembers(User creator, Expedition expedition, List<Long> memberIds) {
        if (expedition != null) {
            List<UserExpedition> userExpeditions =
                    userExpeditionRepository.findAllByExpedition_Id(expedition.getId());
            boolean isMember = userExpeditions.stream()
                    .anyMatch(userExpedition -> userExpedition.getUser().getId().equals(creator.getId()));
            if (!isMember) {
                throw new CapsuleInvalidInputException();
            }
            return userExpeditions.stream()
                    .map(UserExpedition::getUser)
                    .toList();
        }

        if (memberIds != null && memberIds.contains(creator.getId())) {
            throw new CapsuleInvalidInputException();
        }

        List<Long> participantIds = new ArrayList<>();
        participantIds.add(creator.getId());
        if (memberIds != null) {
            participantIds.addAll(memberIds);
        }
        List<User> members = userRepository.findAllByIdForUpdate(participantIds);
        if (members.size() != participantIds.size()) {
            throw new UserNotFoundException();
        }
        return members;
    }

    private void validateDailyLimit(Expedition expedition, List<User> members) {
        LocalDate today = LocalDate.now(KOREA_ZONE_ID);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        if (expedition != null) {
            if (capsuleRepository.countExpeditionCapsulesCreatedBetween(expedition.getId(), start, end) > 0) {
                throw new CapsuleDailyLimitExceededException();
            }
            return;
        }

        List<Long> memberIds = members.stream()
                .map(User::getId)
                .toList();
        if (!capsuleMemberRepository.findPersonalCapsuleUserIdsCreatedBetween(
                memberIds, start, end
        ).isEmpty()) {
            throw new CapsuleDailyLimitExceededException();
        }
    }

    private void saveImages(Capsule capsule, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        List<CapsuleImage> capsuleImages = images.stream()
                .map(image -> CapsuleImage.builder()
                        .capsule(capsule)
                        .image(s3Utils.upload(image, "capsules"))
                        .build())
                .toList();
        capsuleImageRepository.saveAll(capsuleImages);
    }
}
