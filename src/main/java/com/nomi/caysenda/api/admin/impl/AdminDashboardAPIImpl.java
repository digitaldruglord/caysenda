package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminDashboardAPI;
import com.nomi.caysenda.dto.KeywordStatictisDTO;
import com.nomi.caysenda.dto.StatictisTracking;
import com.nomi.caysenda.services.KeywordService;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardAPIImpl implements AdminDashboardAPI {
    @Autowired OrderService orderService;
    @Autowired KeywordService keywordService;
    @Override
    public ResponseEntity<Map> dashboard() {
        Map map = new HashMap();
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> statictisKeywords(Integer page, Integer pageSize, Integer month, Integer year) {
        Page<KeywordStatictisDTO> keys = keywordService.statictis(month,year, page,pageSize );

        return ResponseEntity.ok(Map.of("success",true,"data",keys));
    }

    @Override
    public ResponseEntity<Map> statictisTracking() {
        StatictisTracking tracking = orderService.statictisTracking("currentMonth", LocalDateTime.now().getYear());
        return ResponseEntity.ok(Map.of("success",true,"data",tracking));
    }
}
