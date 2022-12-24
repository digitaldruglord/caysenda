package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliveryOrderApi;
import com.nomi.caysenda.entity.delivery.DeliveryOrderEntity;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/web/delivery/order")
public class DeliveryOrderApiImpl implements DeliveryOrderApi {
    @Autowired DeliveryService deliveryService;
    @Override
    public ResponseEntity<Map> getOrder(String token) {
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.getDeliveryOrder(token)));
    }

    @Override
    public ResponseEntity<Map> getById(String token, Integer id) {
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.findOrderById(token,id)));
    }

    @Override
    public ResponseEntity<Map> search(Integer search) {
        DeliveryOrderEntity deliveryOrderEntity = deliveryService.findOrderById(search);
        if (deliveryOrderEntity!=null){
            return ResponseEntity.ok(Map.of("success",true,"type","HD","data",deliveryOrderEntity));
        }else {
            return ResponseEntity.ok(Map.of("success",false,"type","HD"));
        }
    }
}
