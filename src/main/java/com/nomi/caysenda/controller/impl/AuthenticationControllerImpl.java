package com.nomi.caysenda.controller.impl;

import com.google.common.hash.Hashing;
import com.nomi.caysenda.controller.AuthenticationController;
import com.nomi.caysenda.controller.responses.user.UserChangePasswordResponse;
import com.nomi.caysenda.exceptions.PageNotFountException;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;

@Controller
public class AuthenticationControllerImpl implements AuthenticationController {
    @Autowired
    UserService userService;
    @Override
    public ModelAndView login() {
        ModelAndView view = new ModelAndView("login-register/login");
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null) {
            view.setViewName("redirect:/");
            return view;
        }
        return view;
    }

    @Override
    public ModelAndView register() {
        ModelAndView view = new ModelAndView("login-register/register");
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null) {
            view.setViewName("redirect:/");
            return view;
        }
        return view;
    }

    @Override
    public ModelAndView forgorPassword(Integer id, String hash, Long time) throws PageNotFountException {
        ModelAndView view = new ModelAndView("login-register/forgotpassword");
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(id);
        dataBuilder.append(time);
        String hashOrginal = Hashing.hmacSha256("FORGOT_EMAIL".getBytes(StandardCharsets.UTF_8)).newHasher().putBytes(dataBuilder.toString().getBytes(StandardCharsets.UTF_8)).hash().toString();
        if (!hash.equals(hashOrginal)){
            throw new PageNotFountException();
        }
        view.addObject("id",id);
        view.addObject("hash",hash);
        view.addObject("time",time);
        return view;
    }

    @Override
    public ModelAndView updateFotgotPassword(Integer id, String hash, Long time, String password, String confirmPassword) throws PageNotFountException {
        ModelAndView view = new ModelAndView("login-register/forgotpassword");
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(id);
        dataBuilder.append(time);
        String hashOrginal = Hashing.hmacSha256("FORGOT_EMAIL".getBytes(StandardCharsets.UTF_8)).newHasher().putBytes(dataBuilder.toString().getBytes(StandardCharsets.UTF_8)).hash().toString();
        if (!hash.equals(hashOrginal)){
            throw new PageNotFountException();
        }else {
            UserChangePasswordResponse response = userService.newPassword(id,password,confirmPassword);
            view.addObject("success",response.getStatus());
            view.addObject("message",response.getMessage());

        }
        view.addObject("id",id);
        view.addObject("hash",hash);
        view.addObject("time",time);
        return view;
    }
}
