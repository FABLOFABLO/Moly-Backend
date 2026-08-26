package moly.backend.domain.user.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.presentation.dto.request.UserLoginRequest;
import moly.backend.domain.user.presentation.dto.request.UserSignupRequest;
import moly.backend.domain.user.presentation.dto.response.AccessTokenResponse;
import moly.backend.domain.user.service.UserLoginService;
import moly.backend.domain.user.service.UserProfileImageUpdateService;
import moly.backend.domain.user.service.UserSignupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final UserProfileImageUpdateService userProfileImageUpdateService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody UserSignupRequest request) {
        userSignupService.signup(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        String accessToken = userLoginService.login(request);
        return AccessTokenResponse.from(accessToken);
    }

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void profileUpdate(
            @AuthenticationPrincipal Long id,
            @RequestPart("profileImage") MultipartFile profileImage
    ) {
        userProfileImageUpdateService.update(id, profileImage);
    }
}
