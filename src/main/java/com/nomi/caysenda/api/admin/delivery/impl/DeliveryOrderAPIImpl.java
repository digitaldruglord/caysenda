package com.nomi.caysenda.api.admin.delivery.impl;

import com.nomi.caysenda.api.admin.delivery.DeliveryOrderAPI;
import com.nomi.caysenda.api.admin.delivery.model.*;
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
@RequestMapping("/api/admin/delivery")
public class DeliveryOrderAPIImpl implements DeliveryOrderAPI {
    @Autowired
    DeliveryService deliveryService;

    @Override
    public ResponseEntity<Map> getDelivery(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.findAllDelivery(pageable)));
    }

    @Override
    public ResponseEntity<Map> updateDelivery(UpdateDeliveryRequest params) {
        deliveryService.updateDelivery(params);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> addDeliveryStatus(AddDeliveryStatusRequest params) {
        return ResponseEntity.ok(Map.of("success",true,"data", deliveryService.addDeliveryStatus(params)));
    }

    @Override
    public ResponseEntity<Map> getOrder(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.findAllOrder(pageable)));
    }


    @Override
    public ResponseEntity<Map> updateOrder(UpdateDeliveryOrderRequest params) {
        deliveryService.updateOrder(params);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> addStatus(AddDeliveryOrderStatusRequest params) {

        return ResponseEntity.ok(Map.of("success",true,"data", deliveryService.addDeliveryStatus(params)));
    }

    @Override
    public ResponseEntity<Map> getCurrency(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.findAllCurrency(pageable)));
    }

    @Override
    public ResponseEntity<Map> updateCurrency(UpdateDeliveryCurrencyRequest params) {
        deliveryService.updateCurrency(params);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> changeCurrencyStatus(ChangeStatusCurrencyRequest params) {
        return null;
    }

    @Override
    public ResponseEntity<Map> count(String type) {
        Long count = Long.valueOf(0);
        switch (type){
            case "delivery":
                count = deliveryService.deliveryCount();
                break;
            case "order":
                count = deliveryService.deliveryOrderCount();
                break;
            case "currency":
                count = deliveryService.currencyCount();
                break;
        }
        return ResponseEntity.ok(Map.of("success",true,"count",count));
    }
}
