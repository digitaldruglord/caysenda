package com.nomi.caysenda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

public interface OrderController {
    @GetMapping("/{id}")
    ModelAndView getOrder(@PathVariable("id") Integer id);
}
