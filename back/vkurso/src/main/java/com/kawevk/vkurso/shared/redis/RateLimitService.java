package com.kawevk.vkurso.shared.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final StringRedisTemplate template;

    public RateLimitService(StringRedisTemplate template) {
        this.template = template;
    }

    public boolean isAllowed(String key, int limitRequests, Duration duration) {
        long count = template.opsForValue().increment(key);

        if (count == 1) {
            template.expire(key, duration);
        }

        return count <= limitRequests;
    }

}
