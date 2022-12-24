package com.nomi.caysenda.api.web;

import com.nomi.caysenda.api.web.models.ChangePasswordRequest;
import com.nomi.caysenda.api.web.models.UserDeliveryRegister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface DeliveryUserAPI {
    @GetMapping
    ResponseEntity<Map> getUser(@RequestParam("token") String token);
    @PostMapping("/register")
    ResponseEntity<Map> register(@RequestBody  UserDeliveryRegister deliveryRegister);
    @PostMapping("/login")
    ResponseEntity<Map> login(@RequestParam("username") String userName,
                              @RequestParam("password") String password);
    @PostMapping("/change-password")
    ResponseEntity<Map> changePassword(@RequestBody ChangePasswordRequest request);
}
