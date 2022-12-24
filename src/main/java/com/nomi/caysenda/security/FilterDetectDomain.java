package com.nomi.caysenda.security;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.nomi.caysenda.exceptions.PageNotFountException;
import com.nomi.caysenda.exceptions.authentication.ForbiddenException;
import com.nomi.caysenda.exceptions.authentication.Geo2IPException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class FilterDetectDomain extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    DetectDomainService detectDomainService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String host = "http://"+httpServletRequest.getHeader("host").trim();
        if (!host.equals("http://localhost:8080")) host = host.replace(":8080","");
        host = host.replace("www.","");
        
        httpServletRequest.setAttribute("domain",host)
        ;
        try {
            detectDomainService.detectDomain(host);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        } catch (ForbiddenException e) {
            resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }

    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().contains("api") ||
                request.getRequestURI().contains("authentication") ||
                request.getRequestURI().contains("extention") ||
                request.getRequestURI().contains("ipn") ||
                Pattern.compile(".jpg|.css|.js|.mp4|.mp3|.xlsx|.png|.svg").matcher(request.getRequestURI()).find()){
            return true;
        }
        return false;
    }
}
