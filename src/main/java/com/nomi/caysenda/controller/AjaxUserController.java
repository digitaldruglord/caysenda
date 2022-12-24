package com.nomi.caysenda.controller;

import com.nomi.caysenda.controller.requests.UserRegisterRequest;
import com.nomi.caysenda.controller.responses.UserRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AjaxUserController {
    @GetMapping("/login")
    ResponseEntity<Map> login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpServletRequest request, HttpServletResponse response);
    @GetMapping("/register")
    ResponseEntity<UserRegisterResponse> register(@ModelAttribute UserRegisterRequest registerRequest);
    @GetMapping("/forgotpassword")
    ResponseEntity<Map> forgotPassword(@RequestParam("username") String username);

}
