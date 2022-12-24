package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AdminFacebookAPI {
    @GetMapping("")
    ResponseEntity<Map> findAllUploaded(@RequestParam(value = "page",required = false) Integer page,
                                        @RequestParam(value = "pageSize",required = false) Integer pageSize);
    @GetMapping("/create-post")
    ResponseEntity<Map> createPost(@RequestParam("catId") Integer categoryId);
    @GetMapping("/cancel")
    ResponseEntity<Map> cancel();
    @GetMapping("/delete-post")
    ResponseEntity<Map> deletePost(@RequestParam("catId") Integer categoryId);
    @GetMapping("/count-uploaded")
    ResponseEntity<Map> countUploaded(@RequestParam("catId") Integer catId);
}
