package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.product.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProductControllerAdvice {
    @ExceptionHandler(ProductException.class)
    ResponseEntity<Map> productException(ProductException exception){
        Map map = new HashMap();
        map.put("success",false);
        map.put("code",exception.getCode());
        return new ResponseEntity<>(map, HttpStatus.OK);

    }
}
