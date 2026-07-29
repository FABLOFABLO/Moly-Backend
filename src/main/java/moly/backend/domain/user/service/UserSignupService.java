package moly.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.EmailAlreadyExistsException;
import moly.backend.domain.user.exception.EmailTooLongException;
import moly.backend.domain.user.exception.InvalidSignupInputException;
import moly.backend.domain.user.exception.NicknameAlreadyExistsException;
import moly.backend.domain.user.exception.NicknameTooLongException;
import moly.backend.domain.user.exception.PasswordTooLongException;
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
        validate(email, password, nickname);

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

    private void validate(String email, String password, String nickname) {
        if (email == null || email.isBlank()
                || password == null || password.isBlank()
                || nickname == null || nickname.isBlank()) {
            throw new InvalidSignupInputException();
        }

        if (email.length() > 128) {
            throw new EmailTooLongException();
        }

        if (password.length() > 255) {
            throw new PasswordTooLongException();
        }

        if (nickname.length() > 30) {
            throw new NicknameTooLongException();
        }
    }
}
