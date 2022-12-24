package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.authentication.JWTException;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CartControllerAdvice {
    @ExceptionHandler(AddToCartException.class)
    ResponseEntity<Map> jwtExceptionHandler(AddToCartException e){
        Map map = new HashMap<>();
        map.put("success",false);
        map.put("code",e.getCode());
        map.put("message",e.getMessage());
        map.put("quantity",e.getQuantity());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
