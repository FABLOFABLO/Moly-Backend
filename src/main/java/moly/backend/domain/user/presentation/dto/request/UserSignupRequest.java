package moly.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupRequest(
        @NotBlank
        @Size(max = 128)
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 30)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])\\S+$",
                message = "비밀번호는 영문, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
        )
        String password,
        @NotBlank
        @Size(min = 3, max = 30)
        @Pattern(
                regexp = "^[A-Za-z]+$",
                message = "닉네임은 영문만 사용할 수 있습니다."
        )
        String nickname
) {
}
