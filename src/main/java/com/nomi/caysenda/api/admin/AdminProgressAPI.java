package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AdminProgressAPI {
    @GetMapping()
    ResponseEntity<Map> getProgress(@RequestParam("code") String code);
}
