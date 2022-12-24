package com.nomi.caysenda.config;

import com.nomi.caysenda.interceptors.WebInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    @Qualifier("webInterceptor")
    WebInterceptor webInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInterceptor)
                .excludePathPatterns("/admin/**","/api/**","/resources/**","/authentication/**","/ajax/**","/error","/extention/**")
                .addPathPatterns("/**");
    }
}
