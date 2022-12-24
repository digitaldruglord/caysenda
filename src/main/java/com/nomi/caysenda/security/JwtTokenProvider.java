package com.nomi.caysenda.security;


import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.exceptions.authentication.JWTException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "SYSTEM";
    private final long JWT_EXPIRATION = 86400000L;

    public String generateToken(CustomUserDetail userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
    public String generateToken(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.getSubject());
    }
    public boolean validateToken(String authToken) throws MalformedJwtException,ExpiredJwtException ,UnsupportedJwtException{
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
        return true;

    }
}
