package com.nomi.caysenda.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface DeliveryOrderApi {
    @GetMapping("")
    ResponseEntity<Map> getOrder(@RequestParam("token") String token);
    @GetMapping("/getbyidandtoken")
    ResponseEntity<Map> getById(@RequestParam("token") String token,
                                @RequestParam("id") Integer id);
    @GetMapping("/findbyid")
    ResponseEntity<Map> search(@RequestParam("keyword") Integer search);
}
