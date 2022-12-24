package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminLogAPI;
import com.nomi.caysenda.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/log")
public class AdminLogAPIImpl implements AdminLogAPI {
    @Autowired
    LogService logService;
    @Override
    public ResponseEntity<Map> findAll(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?50:pageSize, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("data",logService.findAll(pageable)));
    }

    @Override
    public ResponseEntity<Map> delete(List<Integer> ids) {
        logService.deleteByIds(ids);
        return ResponseEntity.ok(Map.of("success",true));
    }
}
