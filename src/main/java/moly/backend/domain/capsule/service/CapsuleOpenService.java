package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.ArrivalStatus;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.CapsuleArrival;
import moly.backend.domain.capsule.domain.CapsuleMember;
import moly.backend.domain.capsule.domain.repository.CapsuleArrivalRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleMemberRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.exception.CapsuleAccessDeniedException;
import moly.backend.domain.capsule.exception.CapsuleNotFoundException;
import moly.backend.domain.capsule.exception.CapsuleNotOpenableException;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CapsuleOpenService {
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleArrivalRepository capsuleArrivalRepository;

    @Transactional
    public void execute(Long userId, Long capsuleId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Capsule capsule = capsuleRepository.findByIdForUpdate(capsuleId)
                .orElseThrow(CapsuleNotFoundException::new);
        CapsuleMember member = capsuleMemberRepository.findByCapsule_IdAndUser_Id(capsuleId, user.getId())
                .orElseThrow(CapsuleAccessDeniedException::new);
        if (LocalDateTime.now(KOREA_ZONE_ID).isBefore(capsule.getRequiredTime())) {
            throw new CapsuleNotOpenableException();
        }

        CapsuleArrival requesterArrival = capsuleArrivalRepository.findByCapsuleMember_Id(member.getId())
                .orElseThrow(CapsuleNotOpenableException::new);
        if (requesterArrival.getArrivalStatus() != ArrivalStatus.ARRIVED) {
            throw new CapsuleNotOpenableException();
        }
        long arrivedCount = capsuleArrivalRepository.countByCapsuleIdAndArrivalStatus(
                capsuleId, ArrivalStatus.ARRIVED
        );
        if (arrivedCount < capsule.getRequiredCount()) {
            throw new CapsuleNotOpenableException();
        }
        capsule.open(LocalDateTime.now(KOREA_ZONE_ID));
    }
}
