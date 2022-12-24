package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.product.request.AdminProductRequest;
import com.nomi.caysenda.exceptions.product.ProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;

public interface AdminProductAPI {
    @GetMapping()
    ResponseEntity<Map> findAll(@RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                @RequestParam(value = "keyword",required = false) String keyword,
                                @RequestParam(value = "catId",required = false) Integer catId);
    @GetMapping("/search-product-variant")
    ResponseEntity<Map> searchProductVariant(@RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                @RequestParam(value = "keyword",required = false) String keyword);
    @PostMapping()
    ResponseEntity<Map> create(@RequestBody AdminProductRequest productRequest) throws ProductException;
    @DeleteMapping()
    ResponseEntity<Map> delete(@RequestParam("id") Integer id);
    @GetMapping("/findbyid")
    ResponseEntity<Map> findById(@RequestParam("id") Integer id);
    @PostMapping("/import-excel")
    ResponseEntity<Map> importExcel(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException;
    @GetMapping("/generate-imagezip")
    ResponseEntity<Map> generateImageZip(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                                         @RequestParam(value = "action",required = false)String action);
    @GetMapping("/delete-bycat")
    ResponseEntity<Map> deleteByCat(@RequestParam(value = "categoryId") Integer categoryId);
    @GetMapping("/testcache")
    ResponseEntity<Map> testCache(@RequestParam("id") Integer id);
    @GetMapping("/updatetopflag")
    ResponseEntity<Map> updateTopFlag(@RequestParam("id") Integer id,
                                      @RequestParam("flag") String flag);
}
