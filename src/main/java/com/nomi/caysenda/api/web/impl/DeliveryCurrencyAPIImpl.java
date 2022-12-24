package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliveryCurrencyAPI;
import com.nomi.caysenda.entity.delivery.DeliveryCurrencyEntity;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/web/delivery/currency")
public class DeliveryCurrencyAPIImpl implements DeliveryCurrencyAPI {
    @Autowired
    DeliveryService deliveryService;
    @Override
    public ResponseEntity<Map> findAllOrFindById(Integer id, Integer page, Integer pageSize, String token) {
        Map map = new HashMap();
        if (id!=null){
            DeliveryCurrencyEntity currencyEntity = deliveryService.findCurrencyById(token, id);
            if (currencyEntity!=null){
                map.put("success",true);
                map.put("data",currencyEntity);
            }else {
                map.put("success",false);
            }
        }else {
            Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20);
            Page<DeliveryCurrencyEntity> list = deliveryService.findAllCurrency(token,pageable);
            map.put("success",true);
            map.put("data",list);
        }


        return ResponseEntity.ok(map);
    }
}
