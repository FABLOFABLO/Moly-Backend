package moly.backend.domain.user.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moly.backend.domain.user.presentation.dto.request.UserLoginRequest;
import moly.backend.domain.user.presentation.dto.request.UserSignupRequest;
import moly.backend.domain.user.presentation.dto.response.AccessTokenResponse;
import moly.backend.domain.user.service.UserLoginService;
import moly.backend.domain.user.service.UserSignupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody UserSignupRequest request) {
        userSignupService.signup(request);
    }

    @PostMapping("/login")
    public AccessTokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        String accessToken = userLoginService.login(request);
        return AccessTokenResponse.from(accessToken);
    }
}
