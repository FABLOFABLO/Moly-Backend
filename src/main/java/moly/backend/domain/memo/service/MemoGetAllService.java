package moly.backend.domain.memo.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.memo.domain.repository.MemoRepository;
import moly.backend.domain.memo.presentation.dto.response.MemoListResponse;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoGetAllService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    public MemoListResponse execute(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);

        return MemoListResponse.of(
                user.getMemoCount(),
                memoRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId())
        );
    }
}
