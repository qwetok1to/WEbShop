package com.example.demo.Servise;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.DTO;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.UserResponse;

@Service
public class RedisSerivce {

    private static final Duration USER_TTL = Duration.ofDays(2);

    private final StringRedisTemplate redisTemplate;


    public RedisSerivce(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public AuthDTOS.UserResponse register(DTO dto) {
        validateRegister(dto);

        String id = (dto.id() == null || dto.id().isBlank())? UUID.randomUUID().toString()
                : dto.id().trim();

        String key = userKey(id);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new IllegalStateException("User already exists: " + id);
        }

        redisTemplate.opsForHash().putAll(key, Map.of("email", dto.gmail().trim(),"name", dto.name().trim(),"password", dto.password().trim()));
        redisTemplate.expire(key, USER_TTL);

        return new AuthDTOS.UserResponse(id, dto.gmail().trim(), dto.name().trim());
    }

    public AuthDTOS.UserResponse getUser(String id) {
        Map<Object, Object> data = redisTemplate.opsForHash().entries(userKey(id));
        if (data.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return new AuthDTOS.UserResponse(id,stringValue(data.get("email")),stringValue(data.get("name")));
    }

    public AuthDTOS.UserResponse login(LoginRequest request) {
        if (request == null || isBlank(request.id()) || isBlank(request.password())) {
            throw new IllegalArgumentException("id and password are required");
        }

        String id = request.id().trim();
        String key = userKey(id);
        Object storedPassword = redisTemplate.opsForHash().get(key, "password");
        if (storedPassword == null) {
            throw new IllegalArgumentException("Invalid id or password");
        }

        if (!storedPassword.toString().equals(request.password().trim())) {
            throw new IllegalArgumentException("Invalid id or password");
        }

        redisTemplate.expire(key, USER_TTL);
        return getUser(id);
    }

    public long getTtl(String id) {
        Long ttl = redisTemplate.getExpire(userKey(id));
        return ttl != null ? ttl : -1L;
    }

    private void validateRegister(DTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (isBlank(dto.gmail()) || isBlank(dto.password()) || isBlank(dto.name())) {
            throw new IllegalArgumentException("gmail, password and name are required");
        }
    }

    private static String userKey(String id) {
        return "user:" + id;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String stringValue(Object value) {
        return value == null ? null : value.toString();
    }
}
