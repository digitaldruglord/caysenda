package com.nomi.caysenda.security;

import com.nomi.caysenda.cors.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class MultiHttpSecurityConfig {
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            String[] notIgnoring = new String[]{
                    "/api/admin",
                    "/extention/shop",
                    "/extention/update-sku",
                    "/extention/product",
                    "/extention/findproductbyid",
                    "/extention/employee",
                    "/extention/update-rate-and-factor",
                    "/extention/image-processing",
                    "/extention/delete",
                    "/extention/disable-synchronize",
                    "/extention/deleteproductbyid",
                    "/extention/cart",
                    "/extention/update-product-info",
                    "/extention/update-currency-rate",
                    "/extention/update-enableprice",
                    "/api/ghn",
            };
            return new JwtAuthenticationFilter(notIgnoring);
        }
        @Bean
        public HttpSessionSecurityContextRepository securityContextRepository(){
            return new HttpSessionSecurityContextRepository ();
        }
        @Bean(BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean("passwordEncoder")
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Autowired
        CORSFilter corsFilter;
        protected void configure(HttpSecurity http) throws Exception {
                http
                    .antMatcher("/api/**")
                        .csrf().disable()
                        .cors().disable()

                    .authorizeRequests(authorize -> authorize
                            .antMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN","ROLE_STAFF","ROLE_EMPLOYEE")
                            .and()
                            .addFilterBefore(corsFilter,SecurityContextPersistenceFilter.class)
                            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)

                    )
                    .httpBasic(withDefaults());
        }
    }
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Bean("geo2ipFilter")
        Geo2IpFilter getGeo2IPFilter(){
            return new Geo2IpFilter();
        }
        @Bean("filterDetectDomain")
        FilterDetectDomain getFilterDetectDomain(){
            return new FilterDetectDomain();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                    http.cors().disable()
                        .csrf().disable()
                        .addFilterAfter(getGeo2IPFilter(),SecurityContextPersistenceFilter.class)
                        .addFilterAfter(getFilterDetectDomain(),Geo2IpFilter.class)
                        .authorizeRequests(
                            authorize -> {
                                try {
                                    authorize
                                            .antMatchers("/tai-khoan/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                                            .antMatchers("/ajax/**").permitAll()
                                            .and()
                                            .formLogin()
                                            .loginPage("/dang-nhap").permitAll()
                                            .usernameParameter("username")
                                            .passwordParameter("password")
                                            .defaultSuccessUrl("/")
                                            .failureUrl("/dang-nhap?error").permitAll()
                                            .and()
                                            .logout()
                                            .logoutUrl("/logout");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    );
        }
    }
}
