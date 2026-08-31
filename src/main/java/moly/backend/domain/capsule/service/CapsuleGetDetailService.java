package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.repository.CapsuleImageRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleMemberRepository;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.exception.CapsuleAccessDeniedException;
import moly.backend.domain.capsule.exception.CapsuleNotFoundException;
import moly.backend.domain.capsule.presentation.dto.response.CapsuleDetailResponse;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleGetDetailService {
    private final UserRepository userRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleImageRepository capsuleImageRepository;

    public CapsuleDetailResponse execute(Long userId, Long capsuleId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Capsule capsule = capsuleRepository.findById(capsuleId).orElseThrow(CapsuleNotFoundException::new);
        if (!capsuleMemberRepository.existsByCapsule_IdAndUser_Id(capsuleId, user.getId())) {
            throw new CapsuleAccessDeniedException();
        }
        List<String> images = capsule.getOpenedTime() == null
                ? List.of()
                : capsuleImageRepository.findAllByCapsule_Id(capsuleId).stream()
                        .map(image -> image.getImage())
                        .toList();
        return CapsuleDetailResponse.from(capsule, images);
    }
}
