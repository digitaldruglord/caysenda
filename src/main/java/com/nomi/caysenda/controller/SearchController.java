package com.nomi.caysenda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface SearchController {
    @GetMapping("")
    ResponseEntity<Map> search(@RequestParam("keyword") String keyword);
}
