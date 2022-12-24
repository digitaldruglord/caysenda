package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminPostAPI;
import com.nomi.caysenda.api.admin.model.post.AdminPostRequest;
import com.nomi.caysenda.services.PageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/post")
public class AdminPostAPIImpl implements AdminPostAPI {
    @Autowired
    PageService pageService;
    @Override
    public ResponseEntity<Map> posts(Integer page, Integer pageSize, String type) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        if (type!=null && !type.equals("")){
            return ResponseEntity.ok(Map.of("success",true,"data",pageService.findAll(type,pageable)));
        }else {
            return ResponseEntity.ok(Map.of("success",true,"data",pageService.findAll(pageable)));
        }

    }

    @Override
    public ResponseEntity<Map> deleteById(Integer id) {
        pageService.deleteById(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> findById(Integer id) {
        return ResponseEntity.ok(Map.of("success",true,"data",pageService.findById(id)));
    }

    @Override
    public ResponseEntity<Map> createAndUpdate(AdminPostRequest postRequest) {
        Map map = new HashMap();
        if (postRequest.getName()!=null && !postRequest.getName().equals("")){
            pageService.save(postRequest);
        }

        map.put("success",true);
        return ResponseEntity.ok(map);
    }
}
