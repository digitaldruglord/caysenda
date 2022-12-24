package com.nomi.caysenda.controlleradvice;


import com.nomi.caysenda.exceptions.user.UserLoginException;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserLoginException.class)
    ResponseEntity<Map> userLoginException(UserLoginException exception){
        Map map = new HashMap();
        map.put("success",exception.getStatus());
        map.put("message",exception.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserRegisterException.class)
    ResponseEntity<Map> userRegisterException(UserRegisterException exception){
        Map map = new HashMap();
        map.put("success",false);
        map.put("code",exception.getCode());
        map.put("message",exception.getMessage());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
