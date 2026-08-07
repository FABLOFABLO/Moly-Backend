package moly.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.domain.User;
import moly.backend.domain.user.domain.repository.UserRepository;
import moly.backend.domain.user.exception.InvalidCredentialsException;
import moly.backend.domain.user.presentation.dto.request.UserLoginRequest;
import moly.backend.global.security.jwt.JwtTokenProvider;
import moly.backend.global.security.token.RedisTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenRepository redisTokenRepository;

    @Transactional(readOnly = true)
    public String login(UserLoginRequest request) {
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken =
                jwtTokenProvider.generateAccessToken(user.getNickname());
        redisTokenRepository.saveAccessToken(
                user.getNickname(),
                accessToken,
                jwtTokenProvider.getRemainingDuration(accessToken)
        );
        return accessToken;
    }
}
