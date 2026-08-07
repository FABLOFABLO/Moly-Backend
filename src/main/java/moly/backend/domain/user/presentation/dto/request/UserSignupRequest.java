package moly.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignupRequest(
        @NotBlank
        @Size(max = 128)
        String email,
        @NotBlank
        @Size(max = 255)
        String password,
        @NotBlank
        @Size(max = 30)
        String nickname
) {
}
