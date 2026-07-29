package moly.backend.domain.user.presentation.dto.request;

public record UserSignupRequest(
        String email,
        String password,
        String nickname
) {
}
