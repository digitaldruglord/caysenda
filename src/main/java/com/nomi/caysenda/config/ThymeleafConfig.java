package com.nomi.caysenda.config;

import com.nomi.caysenda.dialect.ProductDialect;
import com.nomi.caysenda.dialect.UrlBuilderDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.FileNotFoundException;

@Configuration
public class ThymeleafConfig {
    @Bean
    SpringSecurityDialect securityDialect(){
        return new SpringSecurityDialect();
    }
    @Bean
    UrlBuilderDialect builderDialect(){
        return new UrlBuilderDialect("urlbuilder");
    }
    @Bean
    ProductDialect productUtilsDialect(){
        return new ProductDialect("productUtils");
    }
    @Bean
    public SpringTemplateEngine templateEngine() throws FileNotFoundException {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(securityDialect());
        templateEngine.addDialect(builderDialect());
        templateEngine.addDialect(productUtilsDialect());
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver() throws FileNotFoundException {
        SpringResourceTemplateResolver templateResolver
                = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() throws FileNotFoundException {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
