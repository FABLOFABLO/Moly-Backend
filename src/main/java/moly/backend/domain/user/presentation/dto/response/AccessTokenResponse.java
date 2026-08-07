package moly.backend.domain.user.presentation.dto.response;

public record AccessTokenResponse(
        String access_token
) {

    public static AccessTokenResponse from(String accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}
