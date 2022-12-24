package com.nomi.caysenda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public interface CartController {
    @GetMapping()
    ModelAndView viewCart();

}
