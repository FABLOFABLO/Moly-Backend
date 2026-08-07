package moly.backend.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moly.backend.global.error.exception.ErrorCode;
import moly.backend.global.security.token.RedisTokenRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String JWT_ERROR_ATTRIBUTE = "jwtErrorCode";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenRepository redisTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!jwtTokenProvider.isAccessToken(token)) {
                request.setAttribute(JWT_ERROR_ATTRIBUTE, ErrorCode.INVALID_TOKEN);
            } else {
                authenticateOrSetRedisError(request, token);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateOrSetRedisError(
            HttpServletRequest request,
            String token
    ) {
        try {
            String subject = jwtTokenProvider.getSubject(token);

            if (!redisTokenRepository.matchesAccessToken(subject, token)) {
                request.setAttribute(JWT_ERROR_ATTRIBUTE, ErrorCode.INVALID_TOKEN);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            subject,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DataAccessException exception) {
            request.setAttribute(
                    JWT_ERROR_ATTRIBUTE,
                    ErrorCode.TOKEN_STORE_UNAVAILABLE
            );
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authorization.substring(BEARER_PREFIX.length());
    }
}
