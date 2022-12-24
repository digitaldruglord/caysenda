package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateDomainRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateSortRequest;
import com.nomi.caysenda.exceptions.category.CategoryException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

public interface AdminCategoryAPI {
    @GetMapping()
    ResponseEntity<Map> findAll(@RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                @RequestParam(value = "keyword",required = false) String keyword,
                                @RequestParam(value = "host",required = false) Integer host);
    @GetMapping("/findcurrentHost")
    ResponseEntity<Map> findAll(@RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                @RequestParam(value = "keyword",required = false) String keyword);
    @PostMapping("")
    ResponseEntity<Map> create(@RequestBody AdminCategoryRequest categoryRequest) throws CategoryException;
    @PostMapping("/update-sorttree")
    ResponseEntity<Map> updateSortTree(@RequestBody AdminCategoryUpdateSortRequest request) throws CategoryException;
    @PutMapping()
    ResponseEntity<Map> update();
    @DeleteMapping()
    ResponseEntity<Map> delete(@RequestParam("id")Integer id) throws CategoryException;
    @GetMapping("/generatezip-thumbnail")
    ResponseEntity<Map> generateThumbnail(@RequestParam("id") Integer categoryId) throws IOException;
    @GetMapping("/generatezip-images")
    ResponseEntity<Map> generateImages(@RequestParam("id") Integer categoryId) throws IOException;
    @PostMapping("/update-host")
    ResponseEntity<Map> updateDomain(@RequestBody AdminCategoryUpdateDomainRequest request);
    @GetMapping("/price-quote")
    ResponseEntity<Map> priceQuote(@RequestParam(value = "category",required = false) Integer category);
    @GetMapping("/generate-shopee-template")
    ResponseEntity<Map> generateShopeeTemplate(@RequestParam("catId") Integer catId,
                                               @RequestParam("shopeeCat") Integer shopeeCat) throws IOException, InvalidFormatException;
    @GetMapping("/shopee-category")
    ResponseEntity<Map> getShopeeCategory(@RequestParam(value = "page",required = false) Integer page,
                                          @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                          @RequestParam(value = "keyword",required = false) String keyword) throws IOException, InvalidFormatException;
    @PostMapping("/shopee-category")
    ResponseEntity<Map> importShopeeCategory() throws IOException, InvalidFormatException;
    @GetMapping("/generate-lazada-template")
    ResponseEntity<Map> generateLazadaTemplate(@RequestParam("catId") Integer catId) throws IOException, InvalidFormatException;
}
