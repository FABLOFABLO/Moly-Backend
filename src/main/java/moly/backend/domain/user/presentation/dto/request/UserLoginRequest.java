package moly.backend.domain.user.presentation.dto.request;

public record UserLoginRequest(
        String nickname,
        String password
) {
}
