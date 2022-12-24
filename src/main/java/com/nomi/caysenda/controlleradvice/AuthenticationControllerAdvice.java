package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.authentication.JWTException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuthenticationControllerAdvice {
    @ExceptionHandler(JWTException.class)
    ResponseEntity<Map> jwtExceptionHandler(JWTException e){
        Map map = new HashMap<>();
        map.put("success",false);
        map.put("code",e.getCode());
        map.put("message",e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MalformedJwtException.class)
    ResponseEntity<Map> jwtEMalformedxceptionHandler(){
        Map map = new HashMap<>();
        map.put("success",false);
        map.put("code","malformed");
        map.put("message","token malformed incorect");
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<Map> jwtExpireExceptionHandler(){
        Map map = new HashMap<>();
        map.put("success",false);
        map.put("code","expire");
        map.put("message","token expire");
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UnsupportedJwtException.class)
    ResponseEntity<Map> jwtUnsupportExceptionHandler(){
        Map map = new HashMap<>();
        map.put("success",false);
        map.put("code","unsupport");
        map.put("message","token unsupport");
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
