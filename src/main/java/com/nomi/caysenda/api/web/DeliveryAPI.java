package com.nomi.caysenda.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface DeliveryAPI {
    @GetMapping("")
    ResponseEntity<Map> getDelivery(@RequestParam("token") String token,
                                    @RequestParam(value = "page",required = false) Integer page,
                                    @RequestParam(value = "pageSize",required = false) Integer pageSize);
    @GetMapping("/getbyidandtoken")
    ResponseEntity<Map> getById(@RequestParam("token") String token,
                                @RequestParam("id") Integer id);
    @GetMapping("/findbyid")
    ResponseEntity<Map> search(@RequestParam("keyword") Integer keyword);

}
