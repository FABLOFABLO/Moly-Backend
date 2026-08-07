package moly.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
        @NotBlank
        @Size(min = 1, max = 30)
        String nickname,
        @NotBlank
        @Size(min = 1, max = 255)
        String password
) {
}
