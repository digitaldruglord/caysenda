package com.nomi.caysenda.ghn.controller.impl;

import com.nomi.caysenda.ghn.controller.GHNController;
import com.nomi.caysenda.ghn.model.request.CreateOrderModelRequest;
import com.nomi.caysenda.ghn.model.request.FeeModelRequest;
import com.nomi.caysenda.ghn.model.responses.OrderInfoModelResponse;
import com.nomi.caysenda.ghn.model.setting.GHNSetttingModel;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.ghn.service.GHNSettingService;
import com.nomi.caysenda.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ghn")
public class GHNControllerImpl implements GHNController {
    @Autowired GHNSettingService ghnSettingService;
    @Autowired GHNService ghnService;
    @Autowired AddressService addressService;
    String KEYSETTING = "KEYSETTING";
    @Override
    public ResponseEntity<Map> settting() {
        Map map = new HashMap();
        map.put("success",true);
        GHNSetttingModel ghnSetttingModel = ghnSettingService.getData(KEYSETTING,GHNSetttingModel.class);
        ghnSetttingModel.getTokens().forEach(ghnTokenModel -> {
            ghnTokenModel.setShops(ghnService.getShopByToken(ghnTokenModel.getToken()));
        });
        map.put("data",ghnSetttingModel);

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateSetting(GHNSetttingModel setttingModel) {
        setttingModel.getTokens().forEach(ghnTokenModel -> {
            ghnTokenModel.setShops(null);
        });
        ghnSettingService.update(setttingModel,KEYSETTING);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getStore(String token) {
        Map map = new HashMap();
        map.put("data",ghnService.getShopByToken(token));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getList(Integer orderId) {
        Map map = new HashMap();
        if (orderId!=null){
            map.put("data",ghnService.getOrder(orderId, Sort.by(Sort.Direction.DESC,"id")));
        }else {
            map.put("data",ghnService.getOrders());
        }

        return ResponseEntity.ok(map);

    }

    @Override
    public ResponseEntity<Map> getOrder(Integer orderId, String ghnOrder) {
        Map map = new HashMap();
        GHNSetttingModel ghnSetttingModel = ghnSettingService.getData(KEYSETTING,GHNSetttingModel.class);
        if (orderId!=null){
            CreateOrderModelRequest modelRequest = ghnService.getData(orderId);
            map.put("data",modelRequest);
            map.put("services",ghnService.getService(ghnSetttingModel.getTokens().get(0).getToken(),modelRequest.getTo_district_id()).getData());
            map.put("dictricts",addressService.dictrcits(modelRequest.getTo_province()));
            map.put("wards",addressService.wards(String.valueOf(modelRequest.getTo_district_id())));
            map.put("token",ghnSetttingModel.getTokens().get(0).getToken());
        }else {
            OrderInfoModelResponse orderInfoModelResponse = ghnService.getDataFromGHN(ghnOrder);
            map.put("data",orderInfoModelResponse.getData());
            map.put("services",ghnService.getService(orderInfoModelResponse.getToken(),orderInfoModelResponse.getData().getTo_district_id()).getData());
            map.put("dictricts",addressService.dictrcits(orderInfoModelResponse.getData().getTo_province()));
            map.put("wards",addressService.wards(String.valueOf(orderInfoModelResponse.getData().getTo_district_id())));
            map.put("token",orderInfoModelResponse.getToken());
        }
        map.put("pickshift",ghnService.getPickShift(String.valueOf(map.get("token"))).getData());
        map.put("provinces",addressService.provinces());
        map.put("setting",ghnSetttingModel);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> createOrder(String token, CreateOrderModelRequest modelRequest) {
        Map map = new HashMap();
        if (modelRequest.getOrder_code()!=null && ghnService.existsByOrder_code(modelRequest.getOrder_code())){
            map.put("data", ghnService.updateOrder(token,modelRequest));
        }else {
            map.put("data", ghnService.createOrder(token,modelRequest));
        }

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateOrder() {
        return null;
    }

    @Override
    public ResponseEntity<Map> cancel(String ghnOrder) {
        Map map = new HashMap();
        map.put("data",ghnService.cancel(ghnOrder));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> tracking(Integer orderId) {
        Map map = new HashMap();
        String tracking = ghnService.tracking(orderId);
        if (tracking!=null){
            map.put("success",true);
            map.put("data",tracking);
        }else {
            map.put("success",false);
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getOrderGHNById(Integer orderId) {
        return ResponseEntity.ok(Map.of("success",true,"data",ghnService.findAllByOrderId(orderId)));
    }

    @Override
    public ResponseEntity<Map> pickShift(String token) {
        Map map = new HashMap();
        map.put("data",ghnService.getPickShift(token).getData());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getFee(String token, FeeModelRequest modelRequest) {
        Map map = new HashMap();
        map.put("data",ghnService.getFee(token,modelRequest).getData());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getService(String token, Integer to_district) {
        Map map = new HashMap();
        map.put("data",ghnService.getService(token,to_district).getData());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> print(String order_code) {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",ghnService.print(order_code));
        return ResponseEntity.ok(map);
    }
}
