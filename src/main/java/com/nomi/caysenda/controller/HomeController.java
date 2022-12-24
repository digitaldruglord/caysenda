package com.nomi.caysenda.controller;

import com.nomi.caysenda.api1688.model.Product1688;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface HomeController {
    @GetMapping("")
    ModelAndView homeView(HttpServletRequest request, HttpServletResponse response) throws AddToCartException;
    @GetMapping("/export-track")
    ResponseEntity<InputStreamResource> exportTrack(@RequestParam("id") List<Integer> ids,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException;
    @GetMapping("/test")
    @ResponseBody
    String test();
}
