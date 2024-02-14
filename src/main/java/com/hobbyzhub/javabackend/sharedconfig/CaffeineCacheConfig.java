package com.hobbyzhub.javabackend.sharedconfig;/*
*
@author ameda
@project backend-modulith
@package com.hobbyzhub.javabackend.sharedconfig
*
*/

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    @Bean
    public Caffeine<Object,Object> caffeine(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats();
    }
    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine());
        return caffeineCacheManager;
    }
}
