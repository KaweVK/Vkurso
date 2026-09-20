package com.kawevk.vkurso.email;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class EmailVerificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SecureRandom random = new SecureRandom();

    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration RESEND_COOLDOWN = Duration.ofSeconds(60);
    private static final int MAX_ATTEMPTS = 5;

    public EmailVerificationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    public void saveCode(String email, String code) {
        String key = codeKey(email);

        redisTemplate.opsForValue().set(
                key,
                code,
                CODE_TTL
        );

        redisTemplate.delete(attemptsKey(email));
    }

    public String getCode(String email) {
        return redisTemplate.opsForValue()
                .get(codeKey(email));
    }

    public void deleteCode(String email) {
        redisTemplate.delete(codeKey(email));
        redisTemplate.delete(attemptsKey(email));
    }

    public boolean hasExceededAttempts(String email) {
        String value = redisTemplate.opsForValue()
                .get(attemptsKey(email));

        if (value == null) {
            return false;
        }

        return Integer.parseInt(value) >= MAX_ATTEMPTS;
    }

    public int incrementAttempts(String email) {
        Long attempts = redisTemplate.opsForValue()
                .increment(attemptsKey(email));

        if (attempts != null && attempts == 1) {
            redisTemplate.expire(
                    attemptsKey(email),
                    CODE_TTL
            );
        }

        return attempts != null ? attempts.intValue() : 0;
    }

    public boolean canResend(String email) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(
                        resendKey(email),
                        "1",
                        RESEND_COOLDOWN
                )
        );
    }

    private String codeKey(String email) {
        return "email-verification:" + email;
    }

    private String attemptsKey(String email) {
        return "email-verification-attempts:" + email;
    }

    private String resendKey(String email) {
        return "email-verification-resend:" + email;
    }
}