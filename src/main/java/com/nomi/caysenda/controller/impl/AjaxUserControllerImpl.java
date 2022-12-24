package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.AjaxUserController;
import com.nomi.caysenda.controller.requests.UserRegisterRequest;
import com.nomi.caysenda.controller.responses.UserRegisterResponse;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.exceptions.user.UserLoginException;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import com.nomi.caysenda.services.CartService;
import com.nomi.caysenda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ajax/user")
public class AjaxUserControllerImpl implements AjaxUserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CartService cartService;
    @Override
    public ResponseEntity<Map> login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        try {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            Map results = userService.login(username,password);
            map.put("success",results.get("success"));
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            cartService.login(sessionId);
        } catch (UserLoginException e) {
            map.put("success",false);
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<UserRegisterResponse> register(UserRegisterRequest registerRequest) {
        try {
            UserRegisterResponse response = userService.register(registerRequest);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getUserName(), registerRequest.getPassword()));
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            return ResponseEntity.ok(response);
        } catch (UserRegisterException e) {
            UserRegisterResponse response = new UserRegisterResponse(false,"Không xác định");
            switch (e.getCode()){
                case UserRegisterException.MISSING_INFOMATION_CODE: response = new UserRegisterResponse(false,"Vui lòng điền đầy đủ thông tin");break;
                case UserRegisterException.PHONE_EXIST_CODE: response = new UserRegisterResponse(false,"Số điện thoại đã tồn tại");break;
                case UserRegisterException.EMAIL_EXIST_CODE: response = new UserRegisterResponse(false,"Email đã tồn tại");break;
                case UserRegisterException.CONFIRM_PASSWORD_INCORECT_CODE: response = new UserRegisterResponse(false,"Kiểm tra lại mật khẩu");break;
                case UserRegisterException.USERNAME_EMAIL_EXIST_CODE: response = new UserRegisterResponse(false,"Tài khoản hoặc email đã tồn tại");break;
            }
            return ResponseEntity.ok(response);
        }
    }


    @Override
    public ResponseEntity<Map> forgotPassword(String username) {
        userService.sendEmailForgotPassword(username);
        return ResponseEntity.ok(Map.of("success",true));
    }
}
