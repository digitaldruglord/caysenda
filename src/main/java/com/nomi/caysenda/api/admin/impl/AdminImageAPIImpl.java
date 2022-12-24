package com.nomi.caysenda.api.admin.impl;


import com.nomi.caysenda.api.admin.AdminImageAPI;
import com.nomi.caysenda.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/image")
public class AdminImageAPIImpl implements AdminImageAPI {
    @Autowired
    ImageService imageService;

    @Override
    public ResponseEntity<Map> findAll(Integer page, Integer pageSize, String keyword, String type) {
        Map map = new HashMap();
        map.put("success",true);
        type  = type!=null?type:"img";
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?20:pageSize, Sort.by(Sort.Direction.DESC,"id"));

        if (keyword!=null){
            map.put("data",imageService.findAllByTypeAndName(type,keyword,pageable ));
        }else {
            map.put("data",imageService.findAllByType(type, pageable));
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> deleteById(Integer id) {
        return new ResponseEntity<>(imageService.deleteById(id),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> uploadImage(MultipartHttpServletRequest request, String type) {
        Map map = new HashMap();
        map.putAll(imageService.upload(request,type));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> uploadZip(MultipartHttpServletRequest request) throws FileNotFoundException {
        imageService.uploadZip(request);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateImages(MultipartHttpServletRequest request) throws IOException {
        imageService.updateImages(request);
        return ResponseEntity.ok(Map.of("success",true));
    }
}
