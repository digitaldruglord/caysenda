package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliveryAPI;
import com.nomi.caysenda.entity.delivery.DeliveryEntity;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/web/delivery")
public class DeliveryAPIImpl implements DeliveryAPI {
    @Autowired
    DeliveryService deliveryService;
    @Override
    public ResponseEntity<Map> getDelivery(String token, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.getDelivery(token,pageable)));
    }

    @Override
    public ResponseEntity<Map> getById(String token, Integer id) {
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.findOrderById(token,id)));
    }

    @Override
    public ResponseEntity<Map> search(Integer keyword) {
        DeliveryEntity deliveryEntity = deliveryService.findDeliveryById(keyword);
        if (deliveryEntity!=null){
            return ResponseEntity.ok(Map.of("success",true,"type","VC","data",deliveryEntity));
        }else {
            return ResponseEntity.ok(Map.of("success",false,"type","VC"));
        }

    }
}
