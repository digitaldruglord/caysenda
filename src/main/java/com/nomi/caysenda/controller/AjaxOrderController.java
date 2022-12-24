package com.nomi.caysenda.controller;

import com.nomi.caysenda.controller.requests.order.BuynowRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AjaxOrderController {
    @PostMapping("/buy-now")
    ResponseEntity<Map> buynow(@RequestBody BuynowRequest buynowRequest);
}
