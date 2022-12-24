package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

public interface AdminDictionaryAPI {
    @GetMapping()
    ResponseEntity<Map> getDictionaries(@RequestParam(value = "page",required = false) Integer page,
                                        @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                        @RequestParam(value = "zhWord",required = false) String zhWord);
    @PostMapping()
    ResponseEntity<Map> updateDictionary(@RequestParam(value = "id",required = false) Integer id,
                                         @RequestParam("zhWord") String zhWord,
                                         @RequestParam("viWord") String viWord);
    @DeleteMapping()
    ResponseEntity<Map> deleteDictionary(@RequestParam("id") Integer id);
    @PostMapping("/upload-excel")
    ResponseEntity<Map> updateExcel(MultipartHttpServletRequest request);
}
