package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminReportAPI;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/report")
public class AdminReportAPIImpl implements AdminReportAPI {
    @Autowired OrderService orderService;
    @Override
    public ResponseEntity<Map> statisticArea(String option, Integer year, Integer host, String status) {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",orderService.statisticByArea(option,year,host, status));
        return ResponseEntity.ok(map);
    }
}
