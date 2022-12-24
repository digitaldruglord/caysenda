package com.nomi.caysenda.controller;

import com.nomi.caysenda.exceptions.PageNotFountException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public interface AuthenticationController {
    @GetMapping("/dang-nhap")
    ModelAndView login();
    @GetMapping("/dang-ky")
    ModelAndView register();
    @GetMapping("/khoi-phuc-mat-khau")
    ModelAndView forgorPassword(@RequestParam("id") Integer id,@RequestParam("hash") String hash,@RequestParam("time") Long time) throws PageNotFountException;
    @PostMapping("/khoi-phuc-mat-khau")
    ModelAndView updateFotgotPassword(@RequestParam("id") Integer id,
                                      @RequestParam("hash") String hash,
                                      @RequestParam("time") Long time,
                                      @RequestParam("newPassword") String password,
                                      @RequestParam("confirmNewPassword") String confirmPassword) throws PageNotFountException;
}
