package com.nomi.caysenda.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nomi.caysenda.exceptions.PageNotFountException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProductController {
    @GetMapping("/san-pham")
    ModelAndView productsView(@RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "sort",required = false)String sort,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                              @RequestParam(value = "keyword",required = false) String keyword,
                              @RequestParam(value = "cat",required = false) String catSlug,
                              HttpServletRequest request);
    @GetMapping("/san-pham-hot")
    ModelAndView hotProducts(@RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "sort",required = false)String sort,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                              @RequestParam(value = "keyword",required = false) String keyword,
                              @RequestParam(value = "cat",required = false) String catSlug,
                              HttpServletRequest request);
    @GetMapping("/{category}/{slug}")
    ModelAndView detailt(@PathVariable("category") String categorySlug,
                         @PathVariable("slug") String productSlug,
                         HttpServletRequest request) throws JsonProcessingException, PageNotFountException;
    @GetMapping("/{category}")
    ModelAndView productsView(@PathVariable("category") String slug,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "keyword",required = false) String keyword,
                              @RequestParam(value = "sort",required = false)String sort,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize,
                              HttpServletRequest request) throws PageNotFountException;
    @GetMapping(value = "/tai-xuong", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<InputStreamResource> downloadImages(@RequestParam("id") Integer id,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response);
}
