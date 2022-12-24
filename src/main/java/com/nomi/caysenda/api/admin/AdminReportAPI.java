package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AdminReportAPI {
    @GetMapping("/statistic-area")
    ResponseEntity<Map> statisticArea(@RequestParam(value = "option",required = false) String option,
                                      @RequestParam(value = "year",required = false) Integer year,
                                      @RequestParam(value = "host",required = false) Integer host,
                                      @RequestParam(value = "status",required = false) String status);
}
