package moly.backend.global.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {

    private static final String ACCESS_KEY_PREFIX = "access:";

    private final StringRedisTemplate redisTemplate;

    public void saveAccessToken(
            String subject,
            String accessToken,
            Duration expiration
    ) {
        redisTemplate.opsForValue().set(
                accessKey(subject),
                accessToken,
                expiration
        );
    }

    public boolean matchesAccessToken(String subject, String accessToken) {
        String savedToken = redisTemplate.opsForValue().get(accessKey(subject));

        return savedToken != null && MessageDigest.isEqual(
                savedToken.getBytes(StandardCharsets.UTF_8),
                accessToken.getBytes(StandardCharsets.UTF_8)
        );
    }

    public void deleteAccessToken(String subject) {
        redisTemplate.delete(accessKey(subject));
    }

    private String accessKey(String subject) {
        return ACCESS_KEY_PREFIX + subject;
    }
}
