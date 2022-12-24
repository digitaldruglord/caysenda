package com.nomi.caysenda.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface DeliveryCurrencyAPI {
    @GetMapping()
    ResponseEntity<Map> findAllOrFindById(@RequestParam(value = "id", required = false) Integer id,
                                          @RequestParam(value = "page",required = false) Integer page,
                                          @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                          @RequestParam("token") String token);
}
