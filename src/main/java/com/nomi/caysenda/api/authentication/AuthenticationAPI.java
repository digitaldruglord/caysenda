package com.nomi.caysenda.api.authentication;

import com.nomi.caysenda.exceptions.user.UserLoginException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Map;

public interface AuthenticationAPI {
    @GetMapping("/login")
    ResponseEntity<Map> login(@RequestParam("username") String userName,
                              @RequestParam("password") String password) throws UserLoginException;
    @GetMapping("/check-token")
    ResponseEntity<Map> checkToken(@RequestParam("token") String token);
}
