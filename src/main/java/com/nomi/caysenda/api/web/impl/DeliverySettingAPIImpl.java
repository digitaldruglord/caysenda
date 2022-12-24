package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliverySettingAPI;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/web/deliverysetting")
public class DeliverySettingAPIImpl implements DeliverySettingAPI {
    @Autowired
    SettingService settingService;
    @Override
    public ResponseEntity<Map> getSetting() {
        return ResponseEntity.ok(settingService.delivery());
    }
}
