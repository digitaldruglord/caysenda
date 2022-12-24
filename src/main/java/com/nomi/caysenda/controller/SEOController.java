package com.nomi.caysenda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SEOController {
    @RequestMapping("/robots.txt")
    void robots(HttpServletRequest request, HttpServletResponse response);
}
