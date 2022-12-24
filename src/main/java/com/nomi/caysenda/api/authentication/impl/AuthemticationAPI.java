package com.nomi.caysenda.api.authentication.impl;

import com.nomi.caysenda.api.authentication.AuthenticationAPI;
import com.nomi.caysenda.exceptions.user.UserLoginException;
import com.nomi.caysenda.security.JwtTokenProvider;
import com.nomi.caysenda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthemticationAPI implements AuthenticationAPI {
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public ResponseEntity<Map> login(String userName, String password) throws UserLoginException {
        Map map = new HashMap();
        map.putAll(userService.login(userName,password));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> checkToken(String token) {

        return ResponseEntity.ok(Map.of("success",jwtTokenProvider.validateToken(token.substring(7))));
    }


}
