package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.authentication.Geo2IPException;
import com.nomi.caysenda.exceptions.category.CategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class Geo2IPControllerAdvice {
    @ExceptionHandler(Geo2IPException.class)
    ResponseEntity<Map> categoryException(Geo2IPException exception){
        Map map = new HashMap();
        map.put("success",false);
        map.put("message","your area is ban");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
