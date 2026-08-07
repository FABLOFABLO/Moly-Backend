package moly.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.EmailAlreadyExistsException;
import moly.backend.domain.user.exception.NicknameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(String email, String password, String nickname) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyExistsException();
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        userRepository.save(user);
    }
}
