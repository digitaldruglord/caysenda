package com.nomi.caysenda.controlleradvice;

import com.nomi.caysenda.exceptions.PageNotFountException;
import com.nomi.caysenda.exceptions.authentication.ForbiddenException;
import com.nomi.caysenda.exceptions.category.CategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
public class NotFoundControllerAdvice {
    @ExceptionHandler(PageNotFountException.class)
    @ResponseStatus( HttpStatus.NOT_FOUND )
    ModelAndView view(){
        ModelAndView view = new ModelAndView("error/notfound");
        return view;
    }
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN )
    Map forbiddenException(){
      return Map.of("success",false);
    }
}
