package com.nomi.caysenda.ghn.controller;

import com.nomi.caysenda.ghn.model.request.CreateOrderModelRequest;
import com.nomi.caysenda.ghn.model.request.FeeModelRequest;
import com.nomi.caysenda.ghn.model.setting.GHNSetttingModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface GHNController {
    @GetMapping("/setting")
    ResponseEntity<Map> settting();
    @PostMapping("/setting")
    ResponseEntity<Map> updateSetting(@RequestBody GHNSetttingModel setttingModel);
    @GetMapping("/store")
    ResponseEntity<Map> getStore(@RequestParam("token") String token);
    @GetMapping("/list")
    ResponseEntity<Map> getList(@RequestParam(value = "orderId",required = false)Integer orderId );
    @GetMapping("/order")
    ResponseEntity<Map> getOrder(@RequestParam(value = "orderId",required = false) Integer orderId,
                                 @RequestParam(value = "ghnOrder",required = false) String ghnOrder);
    @PostMapping("/order")
    ResponseEntity<Map> createOrder(@RequestParam("token") String token,
                                    @RequestBody CreateOrderModelRequest modelRequest);
    @PutMapping("/order")
    ResponseEntity<Map> updateOrder();
    @GetMapping("/pick-shift")
    ResponseEntity<Map> pickShift(@RequestParam("token") String token);
    @PostMapping("/fee")
    ResponseEntity<Map> getFee(@RequestParam("token") String token, @RequestBody FeeModelRequest modelRequest);
    @GetMapping("/service")
    ResponseEntity<Map> getService(@RequestParam("token") String token,
                                   @RequestParam("to_district") Integer to_district);
    @GetMapping("/print")
    ResponseEntity<Map> print(@RequestParam("order_code")String order_code);
    @GetMapping("/cancel")
    ResponseEntity<Map> cancel(@RequestParam("order_code")String order_code);
    @GetMapping("/tracking")
    ResponseEntity<Map> tracking(@RequestParam("orderId")Integer orderId);
    @GetMapping("/get-list-order-byid")
    ResponseEntity<Map> getOrderGHNById(@RequestParam("orderId")Integer orderId);

}
