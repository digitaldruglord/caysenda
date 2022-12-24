package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliveryContactFormAPI;
import com.nomi.caysenda.api.web.models.ContactFormRequest;
import com.nomi.caysenda.entity.delivery.DeliveryCurrencyEntity;
import com.nomi.caysenda.entity.delivery.DeliveryEntity;
import com.nomi.caysenda.entity.delivery.DeliveryOrderEntity;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/web/delivery/form")
public class DeliveryContactFormAPIImpl implements DeliveryContactFormAPI {
    @Autowired
    DeliveryService deliveryService;

    @Override
    public ResponseEntity<Map> contactForm(ContactFormRequest formRequest) {
        Map  map = new HashMap();
        map.put("success",false);
        switch (formRequest.getType()){
            case "order":
                DeliveryOrderEntity formEntity = deliveryService.createOrder(formRequest);
                map.put("success",formEntity!=null?true:false);
                break;
            case "currency":
                DeliveryCurrencyEntity currencyEntity = deliveryService.createCurrency(formRequest);
                map.put("success",currencyEntity!=null?true:false);
                break;
            case "delivery":
                DeliveryEntity deliveryEntity = deliveryService.createDelivery(formRequest);
                map.put("success",deliveryEntity!=null?true:false);
                break;
        }
        return ResponseEntity.ok(map);
    }

}
