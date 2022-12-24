package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminProgressAPI;
import com.nomi.caysenda.redis.model.RedisProgress;
import com.nomi.caysenda.redis.repositories.RedisProgressRepository;
import com.nomi.caysenda.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/progress")
public class AdminProgressAPIImpl implements AdminProgressAPI {
    @Autowired
    RedisProgressRepository redisProgressRepository;
    @Autowired
    ProgressService progressService;
    @Override
    public ResponseEntity<Map> getProgress(String code) {
        Map map = new HashMap();
        RedisProgress redisProgress = progressService.findByCode(code);
        if (redisProgress!=null){
            map.put("success",true);
            map.put("data",redisProgress);
        }else {
            map.put("success",false);
        }

        return ResponseEntity.ok(map);
    }
}
