package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface AdminImageAPI {
    @GetMapping()
    ResponseEntity<Map> findAll(@RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "keyword",required = false)String keyword,
                                @RequestParam(value = "type",required = false) String type);
    @DeleteMapping()
    ResponseEntity<Map> deleteById(@RequestParam("id") Integer id);
    @RequestMapping("/upload")
    ResponseEntity<Map> uploadImage(MultipartHttpServletRequest request,
                                    @RequestParam(value = "type",required = false) String type);
    @RequestMapping("/uploadzip")
    ResponseEntity<Map> uploadZip(MultipartHttpServletRequest request) throws FileNotFoundException;
    @RequestMapping("/update")
    ResponseEntity<Map> updateImages(MultipartHttpServletRequest request) throws IOException;
}
