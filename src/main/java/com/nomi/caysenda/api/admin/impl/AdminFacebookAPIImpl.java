package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminFacebookAPI;
import com.nomi.caysenda.services.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/facebook")
public class AdminFacebookAPIImpl implements AdminFacebookAPI {
    @Autowired FacebookService facebookService;

    @Override
    public ResponseEntity<Map> findAllUploaded(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:50, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("success",true,"data",facebookService.findAllUploaded(pageable)));
    }

    @Override
    public ResponseEntity<Map> createPost(Integer categoryId) {
       facebookService.createPostByCat(categoryId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> cancel() {
        facebookService.cancelRunning();
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deletePost(Integer categoryId) {
        facebookService.deletePostByCat(categoryId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> countUploaded(Integer catId) {

        return ResponseEntity.ok(Map.of("success",true,"count",facebookService.countUploaded(catId)));
    }
}
