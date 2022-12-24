package com.nomi.caysenda.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class StaticResources implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/static/css/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/scss/**").addResourceLocations("classpath:/static/scss/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/vendor/**").addResourceLocations("classpath:/static/vendor/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/fonts/**").addResourceLocations("classpath:/static/fonts/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/img/**").addResourceLocations("classpath:/static/img/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/static/js/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/svg/**").addResourceLocations("classpath:/static/svg/");
        registry.addResourceHandler("/resources/sitemap/**").addResourceLocations("classpath:/static/sitemap/");
        registry.addResourceHandler("/resources/upload/**").addResourceLocations("classpath:/static/upload/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/video/**").addResourceLocations("classpath:/static/video/");
        registry.addResourceHandler("/resources/excel/**").addResourceLocations("classpath:/static/excel/");
        registry.addResourceHandler("/resources/zip/**").addResourceLocations("classpath:/static/zip/");
        registry.addResourceHandler("/resources/shopeetemplates/**").addResourceLocations("classpath:/static/shopeetemplates/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:/static/robots.txt");
    }
}
