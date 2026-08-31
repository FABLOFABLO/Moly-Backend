package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.CapsuleArrival;
import moly.backend.domain.capsule.domain.CapsuleMember;
import moly.backend.domain.capsule.domain.repository.CapsuleArrivalRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleMemberRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.exception.CapsuleAccessDeniedException;
import moly.backend.domain.capsule.exception.CapsuleAlreadyOpenedException;
import moly.backend.domain.capsule.exception.CapsuleInvalidArrivalStatusException;
import moly.backend.domain.capsule.exception.CapsuleNotFoundException;
import moly.backend.domain.capsule.presentation.dto.request.CapsuleLocationRequest;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CapsuleDepartureService {
    private static final double ARRIVAL_RADIUS_METERS = 50.0;
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleArrivalRepository capsuleArrivalRepository;

    @Transactional
    public void execute(Long userId, Long capsuleId, CapsuleLocationRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Capsule capsule = capsuleRepository.findById(capsuleId).orElseThrow(CapsuleNotFoundException::new);
        if (capsule.getOpenedTime() != null) {
            throw new CapsuleAlreadyOpenedException();
        }
        if (capsuleRepository.isWithinArrivalRange(
                capsuleId,
                request.longitude(),
                request.latitude(),
                ARRIVAL_RADIUS_METERS
        )) {
            throw new CapsuleInvalidArrivalStatusException();
        }

        CapsuleMember member = capsuleMemberRepository.findByCapsule_IdAndUser_Id(capsuleId, user.getId())
                .orElseThrow(CapsuleAccessDeniedException::new);
        CapsuleArrival arrival = capsuleArrivalRepository.findByCapsuleMember_Id(member.getId())
                .orElseThrow(CapsuleInvalidArrivalStatusException::new);
        arrival.depart(LocalDateTime.now(KOREA_ZONE_ID));
    }
}
