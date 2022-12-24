package com.nomi.caysenda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AjaxController {
    @GetMapping("/lazy/product-lazy")
    ResponseEntity<Map> ProductRandlazyLoad(@RequestParam("tab") String tab,
                                            @RequestParam(value = "categoryId",required = false) Integer categoryId);
}
