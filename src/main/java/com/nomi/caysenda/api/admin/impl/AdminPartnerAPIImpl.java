package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminPartnerAPI;
import com.nomi.caysenda.api.admin.model.partner.PartnerRequest;
import com.nomi.caysenda.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/partner")
public class AdminPartnerAPIImpl implements AdminPartnerAPI {
    @Autowired
    PartnerService partnerService;
    @Override
    public ResponseEntity<Map> findAll() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",partnerService.findAll());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> save(PartnerRequest partnerRequest) {
        Map map = new HashMap();
        map.put("success",true);
        if (partnerRequest.getId()!=null){
            map.put("type","update");
            map.put("data",partnerService.update(partnerRequest));
        }else {
            map.put("type","create");
            map.put("data",partnerService.save(partnerRequest));
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> delete(Integer id) {
        Map map = new HashMap();
        partnerService.deleteById(id);
        map.put("success",true);
        return ResponseEntity.ok(map);
    }
}
