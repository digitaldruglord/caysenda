package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.category.CategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CategoryControllerAdvice {
    @ExceptionHandler(CategoryException.class)
    ResponseEntity<Map> categoryException(CategoryException exception){
        Map map = new HashMap();
        map.put("success",false);
        map.put("code",exception.getCode());
        map.put("message",exception.getMessage());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
