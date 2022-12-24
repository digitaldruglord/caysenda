package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.options.model.LazadaSetting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface AdminLazadaAPI {
    @GetMapping("")
    ResponseEntity<Map> getData();
    @PostMapping("")
    ResponseEntity<Map> updateData(@RequestBody LazadaSetting lazadaSetting);
    @GetMapping("/token")
    ResponseEntity<Map> getToken(@RequestParam("code") String code) throws ApiException;
    @PostMapping("/token")
    ResponseEntity<Map> updateToken(@RequestParam("token") String token);
    @GetMapping("/categories")
    ResponseEntity<Map> getCategories() throws ApiException;
    @RequestMapping("/uptolazada")
    ResponseEntity<Map> uptoLazada(@RequestParam("categoryId") Integer categoryId,
                                   @RequestParam("lazadaCategoryId") Integer lazadaCategoryId) throws Exception;
    @RequestMapping("/removeproduct")
    ResponseEntity<Map> removeProduct(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                                      @RequestParam(value = "sku",required = false) String sku) throws Exception;
}
