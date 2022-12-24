package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.AjaxOrderController;
import com.nomi.caysenda.controller.requests.order.BuynowRequest;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ajax/order")
public class AjaxOrderControllerImpl implements AjaxOrderController {
    @Autowired
    OrderService orderService;
    @Override
    public ResponseEntity<Map> buynow(BuynowRequest buynowRequest) {
        Map map = new HashMap();
        OrderEntity orderEntity = orderService.buynow(buynowRequest);
        if (orderEntity!=null){
            map.put("success",true);
            map.put("id",orderEntity.getId());
        }else {
            map.put("success",false);
        }
        return ResponseEntity.ok(map);
    }
}
