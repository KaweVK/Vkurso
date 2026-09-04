package com.kawevk.vkurso.shared.config;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.courseCategory.dtos.CourseCategoryResponse;
import com.kawevk.vkurso.lesson.dtos.LessonResponse;
import com.kawevk.vkurso.module.dtos.ModuleResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("courses", cacheConfiguration(CourseResponse.class))
                .withCacheConfiguration("modules", cacheConfiguration(ModuleResponse.class))
                .withCacheConfiguration("lessons", cacheConfiguration(LessonResponse.class))
                .withCacheConfiguration("courseCategories", cacheConfiguration(CourseCategoryResponse.class))
                .withCacheConfiguration("enrollments", cacheConfiguration(Boolean.class))
                .build();
    }

    private <T> RedisCacheConfiguration cacheConfiguration(Class<T> valueType) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new JacksonJsonRedisSerializer<>(valueType)
                        )
                );
    }
}
