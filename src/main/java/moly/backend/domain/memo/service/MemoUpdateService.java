package moly.backend.domain.memo.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.memo.domain.Memo;
import moly.backend.domain.memo.domain.repository.MemoRepository;
import moly.backend.domain.memo.exception.MemoAccessDeniedException;
import moly.backend.domain.memo.exception.MemoNotFoundException;
import moly.backend.domain.memo.presentation.dto.request.MemoUpdateRequest;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoUpdateService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(String nickname, Long memoId, MemoUpdateRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);

        validateOwner(user, memo);
        memo.updateContent(request.content());
    }

    private void validateOwner(User user, Memo memo) {
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new MemoAccessDeniedException();
        }
    }
}
