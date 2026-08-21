package moly.backend.domain.memo.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.memo.domain.Memo;
import moly.backend.domain.memo.domain.repository.MemoRepository;
import moly.backend.domain.memo.exception.MemoAccessDeniedException;
import moly.backend.domain.memo.exception.MemoNotFoundException;
import moly.backend.domain.memo.presentation.dto.response.MemoResponse;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoGetDetailService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    public MemoResponse execute(String nickname, Long memoId) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);

        validateOwner(user, memo);
        return MemoResponse.from(memo);
    }

    private void validateOwner(User user, Memo memo) {
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new MemoAccessDeniedException();
        }
    }
}
