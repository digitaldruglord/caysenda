package com.nomi.caysenda.api.admin.delivery;

import com.nomi.caysenda.api.admin.delivery.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface DeliveryOrderAPI {
    @GetMapping("")
    ResponseEntity<Map> getDelivery(@RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "pageSize",required = false) Integer pageSize);
    @PutMapping("")
    ResponseEntity<Map> updateDelivery(@RequestBody UpdateDeliveryRequest params);
    @PostMapping("/addstatus")
    ResponseEntity<Map> addDeliveryStatus(@RequestBody AddDeliveryStatusRequest params);

    @GetMapping("/order")
    ResponseEntity<Map> getOrder(@RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "pageSize",required = false) Integer pageSize);
    @PutMapping("/order")
    ResponseEntity<Map> updateOrder(@RequestBody UpdateDeliveryOrderRequest params);
    @PostMapping("/order/addstatus")
    ResponseEntity<Map> addStatus(@RequestBody AddDeliveryOrderStatusRequest params);

    @GetMapping("/currency")
    ResponseEntity<Map> getCurrency(@RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "pageSize",required = false) Integer pageSize);
    @PutMapping("/currency")
    ResponseEntity<Map> updateCurrency(@RequestBody UpdateDeliveryCurrencyRequest params);
    @PostMapping("/changecurrencystatus")
    ResponseEntity<Map> changeCurrencyStatus(@RequestBody ChangeStatusCurrencyRequest params);
    @GetMapping("/count")
    ResponseEntity<Map> count(@RequestParam(value = "type",required = false) String type);
}
