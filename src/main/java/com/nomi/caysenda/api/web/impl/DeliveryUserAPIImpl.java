package com.nomi.caysenda.api.web.impl;

import com.nomi.caysenda.api.web.DeliveryUserAPI;
import com.nomi.caysenda.api.web.models.ChangePasswordRequest;
import com.nomi.caysenda.api.web.models.UserDeliveryRegister;
import com.nomi.caysenda.services.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/web/delivery/user")
public class DeliveryUserAPIImpl implements DeliveryUserAPI {
    @Autowired
    DeliveryService deliveryService;

    @Override
    public ResponseEntity<Map> getUser(String token) {
        return ResponseEntity.ok(Map.of("success",true,"data",deliveryService.getUserDelivery(token)));
    }

    @Override
    public ResponseEntity<Map> register(UserDeliveryRegister deliveryRegister) {
        return ResponseEntity.ok(deliveryService.register(deliveryRegister));
    }

    @Override
    public ResponseEntity<Map> login(String userName, String password) {

        return ResponseEntity.ok(deliveryService.login(userName, password));
    }

    @Override
    public ResponseEntity<Map> changePassword(ChangePasswordRequest request) {
        return ResponseEntity.ok(deliveryService.changePassword(request));
    }
}
