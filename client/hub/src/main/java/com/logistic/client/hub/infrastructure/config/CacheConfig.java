package com.logistic.client.hub.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public ObjectMapper customObjectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean
  public RedisSerializer<Object> springDataRedisSerializer(ObjectMapper customObjectMapper) {
    return new GenericJackson2JsonRedisSerializer(customObjectMapper);
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
      RedisSerializer<Object> springDataRedisSerializer // 위에서 만든 Bean 자동 주입
  ) {

    RedisCacheConfiguration hubCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofHours(1))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(SerializationPair.fromSerializer(springDataRedisSerializer));

    RedisCacheConfiguration allHubsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofHours(2))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(SerializationPair.fromSerializer(springDataRedisSerializer));

    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    cacheConfigurations.put("hubCache", hubCacheConfig);
    cacheConfigurations.put("allHubsCache", allHubsCacheConfig);

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(hubCacheConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

}