package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.WebProductAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/web/product")
public class WebProductAPIImpl implements WebProductAPI {
    @Override
    public ResponseEntity<Map> products() {
        Map map = new HashMap();

        
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
