package com.nomi.caysenda.security;

import com.nomi.caysenda.exceptions.authentication.JWTException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    String[] notIgnoring;
    public JwtAuthenticationFilter(String[] notIgnoring) {
        this.notIgnoring = notIgnoring;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String jwt = getJwtFromRequest(request);
        try {
            if (StringUtils.hasText(jwt) ) {
                tokenProvider.validateToken(jwt);
                String userName = tokenProvider.getUserIdFromJWT(jwt);
                CustomUserDetail userDetails = customUserDetailService.loadUserByUsername(userName);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }else {
                throw new JWTException("token wrong format","tokenwrongformat");
            }
            filterChain.doFilter(request, response);
        } catch (JWTException e) {
            resolver.resolveException(request, response, null, e);
        }catch (MalformedJwtException e) {
            resolver.resolveException(request, response, null, e);
        }catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null, e);
        }catch (UnsupportedJwtException e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (String url:notIgnoring){
            if (Pattern.compile(url).matcher(request.getRequestURI()).find()){
                return false;
            }
        }
        return true;
    }
}
