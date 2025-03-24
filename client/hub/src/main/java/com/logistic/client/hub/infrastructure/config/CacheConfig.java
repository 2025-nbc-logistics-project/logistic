package com.logistic.client.hub.infrastructure.config;

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
import org.springframework.data.redis.serializer.RedisSerializer;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

    RedisCacheConfiguration hubCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofHours(1))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()));

    RedisCacheConfiguration allHubsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofHours(2))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()));

    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    cacheConfigurations.put("hubCache", hubCacheConfig);
    cacheConfigurations.put("allHubsCache", allHubsCacheConfig);

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(hubCacheConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

}