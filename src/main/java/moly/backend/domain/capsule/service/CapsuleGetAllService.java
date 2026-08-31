package moly.backend.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.capsule.domain.Capsule;
import moly.backend.domain.capsule.domain.repository.CapsuleRepository;
import moly.backend.domain.capsule.presentation.dto.response.CapsuleListItemResponse;
import moly.backend.domain.capsule.presentation.dto.response.CapsuleListResponse;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleGetAllService {
    private final CapsuleRepository capsuleRepository;
    private final UserRepository userRepository;

    public CapsuleListResponse execute(Long userId, boolean isOpened) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Capsule> capsules = isOpened
                ? capsuleRepository.findOpenedByMemberUserId(user.getId())
                : capsuleRepository.findUnopenedByMemberUserId(user.getId());
        return new CapsuleListResponse(capsules.stream().map(CapsuleListItemResponse::from).toList());
    }
}
