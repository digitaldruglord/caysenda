package com.nomi.caysenda.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class RedisCacheConfig {
    @Bean("restTemplate")
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "options",
                "product","priceSetting","websiteSetting",
                "slideSetting","bannerSetting","brandSetting",
                "embedHeaderSetting","embedSocial",
                "menuSetting","productscache","topkeyword",
                "geo2ip"
        );
    }

}
