package com.nomi.caysenda.api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public interface DeliverySettingAPI {
    @GetMapping
    ResponseEntity<Map> getSetting();
}
