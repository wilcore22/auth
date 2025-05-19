package auth.session.infraestructure.persistence.redis;


import auth.session.application.ports.output.SessionRepositoryPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisSessionRepositoryAdapter implements SessionRepositoryPort {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisSessionRepositoryAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveSession(String token, String userId, long expirationTime) {
        redisTemplate.opsForValue().set(token, userId, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getUserIdFromToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    @Override
    public void deleteSession(String token) {
        redisTemplate.delete(token);
    }
}
