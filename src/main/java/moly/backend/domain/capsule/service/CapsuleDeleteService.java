package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleArrivalRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleImageRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleMemberRepository;
import moly.backend.domain.capsule.exception.CapsuleAccessDeniedException;
import moly.backend.domain.capsule.exception.CapsuleAlreadyOpenedException;
import moly.backend.domain.capsule.exception.CapsuleNotOpenableException;
import moly.backend.domain.capsule.exception.CapsuleNotFoundException;
import moly.backend.domain.expedition.domain.ExpeditionRole;
import moly.backend.domain.expedition.domain.repository.UserExpeditionRepository;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CapsuleDeleteService {
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleArrivalRepository capsuleArrivalRepository;
    private final CapsuleImageRepository capsuleImageRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final UserExpeditionRepository userExpeditionRepository;

    @Transactional
    public void execute(Long userId, Long capsuleId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Capsule capsule = capsuleRepository.findById(capsuleId).orElseThrow(CapsuleNotFoundException::new);
        if (capsule.getOpenedTime() != null) {
            throw new CapsuleAlreadyOpenedException();
        }
        if (capsule.getCreatedAt().plusDays(7).isBefore(LocalDateTime.now(KOREA_ZONE_ID))) {
            throw new CapsuleNotOpenableException();
        }
        validateDeletePermission(capsule, user);
        capsuleArrivalRepository.deleteAllByCapsuleId(capsuleId);
        capsuleImageRepository.deleteAllByCapsuleId(capsuleId);
        capsuleMemberRepository.deleteAllByCapsuleId(capsuleId);
        capsuleRepository.delete(capsule);
    }

    private void validateDeletePermission(Capsule capsule, User user) {
        if (capsule.getExpedition() == null) {
            if (!capsuleMemberRepository.existsByCapsule_IdAndUser_Id(capsule.getId(), user.getId())) {
                throw new CapsuleAccessDeniedException();
            }
            return;
        }

        boolean isLeader = userExpeditionRepository.findByExpedition_IdAndUser_Id(
                        capsule.getExpedition().getId(), user.getId()
                )
                .map(userExpedition -> userExpedition.getExpeditionRole() == ExpeditionRole.LEADER)
                .orElse(false);
        if (!isLeader) {
            throw new CapsuleAccessDeniedException();
        }
    }
}
