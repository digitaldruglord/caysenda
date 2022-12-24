package com.nomi.caysenda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface PrintController {
    @GetMapping("")
    ModelAndView print(@RequestParam("id")List<Integer> ids);
}
