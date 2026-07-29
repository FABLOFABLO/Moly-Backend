package moly.backend.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moly.backend.global.error.exception.ErrorCode;
import moly.backend.global.security.jwt.JwtAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityErrorResponseWriter responseWriter;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException
    ) throws IOException {
        Object errorAttribute = request.getAttribute(
                JwtAuthenticationFilter.JWT_ERROR_ATTRIBUTE
        );
        ErrorCode errorCode = errorAttribute instanceof ErrorCode code
                ? code
                : ErrorCode.AUTHENTICATION_REQUIRED;

        responseWriter.write(response, errorCode);
    }
}
