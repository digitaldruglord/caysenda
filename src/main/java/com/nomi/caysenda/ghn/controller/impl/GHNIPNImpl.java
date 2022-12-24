package com.nomi.caysenda.ghn.controller.impl;

import com.nomi.caysenda.ghn.controller.GHNIPN;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/ghn")
public class GHNIPNImpl implements GHNIPN {
    @Autowired GHNService ghnService;
    @Override
    public ResponseEntity<Map> ipn(Map webhook) {
        Map map = new HashMap();
        ghnService.webhook(webhook);
        return ResponseEntity.ok(map);
    }
}
