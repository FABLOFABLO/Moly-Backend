package moly.backend.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";

    private final SecretKey secretKey;
    private final long accessExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-ms}") long accessExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpirationMs = accessExpirationMs;
    }

    public String generateAccessToken(String subject) {
        return generateToken(subject, ACCESS_TOKEN_TYPE, accessExpirationMs);
    }

    public boolean isAccessToken(String token) {
        return isTokenType(token, ACCESS_TOKEN_TYPE);
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Duration getRemainingDuration(String token) {
        long remainingMs = parseClaims(token).getExpiration().getTime()
                - System.currentTimeMillis();
        return Duration.ofMillis(Math.max(remainingMs, 0));
    }

    private String generateToken(String subject, String tokenType, long expirationMs) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMs);

        return Jwts.builder()
                .subject(subject)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private boolean isTokenType(String token, String expectedType) {
        try {
            String tokenType = parseClaims(token).get(
                    TOKEN_TYPE_CLAIM,
                    String.class
            );
            return expectedType.equals(tokenType);
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
