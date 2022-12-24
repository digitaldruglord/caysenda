package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


public interface AdminDashboardAPI {
    @GetMapping()
    ResponseEntity<Map> dashboard();
    @GetMapping("/keywords")
    ResponseEntity<Map> statictisKeywords(@RequestParam(value = "page",required = false) Integer page,
                                          @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                          @RequestParam(value = "month",required = false) Integer month,
                                          @RequestParam(value = "year",required = false) Integer year);
    @GetMapping("/statictis-tracking")
    ResponseEntity<Map> statictisTracking();
}
