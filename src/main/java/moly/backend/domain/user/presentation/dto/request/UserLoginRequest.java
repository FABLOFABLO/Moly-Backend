package moly.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank
        String nickname,
        @NotBlank
        String password
) {
}
