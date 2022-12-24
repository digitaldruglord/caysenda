package com.nomi.caysenda.ghn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface GHNIPN {
    @PostMapping("/ipn")
    ResponseEntity<Map> ipn(@RequestBody Map webhook);
}
