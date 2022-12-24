package com.nomi.caysenda.controller;

import com.nomi.caysenda.exceptions.PageNotFountException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public interface PostController {
    @GetMapping()
    ModelAndView posts(@RequestParam(value = "page",required = false) Integer page,
                       @RequestParam(value = "pageSize",required = false) Integer pageSize,
                       HttpServletRequest request);
    @GetMapping("/{slug}")
    ModelAndView postDetailt(@PathVariable("slug") String slug) throws PageNotFountException;
}
