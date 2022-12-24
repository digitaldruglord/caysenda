package com.nomi.caysenda.controller;

import com.nomi.caysenda.controller.requests.user.UserUpdateRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserController {
    @GetMapping()
    ModelAndView user();
    @PostMapping()
    ModelAndView updateUser(@ModelAttribute UserUpdateRequest updateRequest);
    @GetMapping("/dang-nhap")
    ModelAndView login();
    @GetMapping("/dang-xuat")
    ModelAndView logout(HttpServletRequest request, HttpServletResponse response);
    @GetMapping("/hoa-don")
    ModelAndView orders(@RequestParam(value = "page",required = false) Integer page,
                        @RequestParam(value = "pageSize",required = false) Integer pageSize,
                        HttpServletRequest request);
    @GetMapping("/hoa-don/{id}")
    ModelAndView orderDetailt(@PathVariable("id") Integer orderId);
    @GetMapping("/dia-chi")
    ModelAndView address();
    @GetMapping("/tai-anh-dai-dien")
    ModelAndView downloadImage();
    @GetMapping("/tai-bao-gia")
    ModelAndView downloadPriceQuote();
    @GetMapping("/tai-bao-gia-kem-hinh-anh")
    ModelAndView downloadPriceQuoteWithThumbnail();
    @GetMapping("/tai-toan-bo-anh")
    ModelAndView downloadImages();
    @GetMapping("/tai-anh-dai-dien-sku")
    ModelAndView downloadImageSKU();
    @GetMapping("/tai-toan-bo-anh-sku")
    ModelAndView downloadImagesSKU();
    @GetMapping("/doi-mat-khau")
    ModelAndView changePassword();
    @PostMapping("/doi-mat-khau")
    ModelAndView updateChangePassword(@RequestParam("passwordold")String passwordOld,
                                      @RequestParam("passwordnew")String passwordNew,
                                      @RequestParam("passwordnewconfirm")String passwordNewConfirm);
//    @GetMapping("/export-order")

    @GetMapping(value = "/export-order", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<InputStreamResource> downloadExcel(@RequestParam("id") Integer id,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response);

    @GetMapping(value = "/download-image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<InputStreamResource> downloadImages(@RequestParam("id") Integer id,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response);
}
