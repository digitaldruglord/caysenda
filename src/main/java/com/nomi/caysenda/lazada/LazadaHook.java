package com.nomi.caysenda.lazada;


import com.google.gson.Gson;
import com.nomi.caysenda.lazada.services.LazadaOrderService;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.lazada.services.model.LazadaGenerateToken;
import com.nomi.caysenda.lazada.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@Controller
@RequestMapping("/lazada-hook")
public class LazadaHook {
    @Autowired
    LazadaSystemService lazadaSystemService;
    @Autowired
    LazadaOrderService lazadaOrderService;
    @GetMapping("/generate-token")
    ResponseEntity<LazadaGenerateToken> generateToken(@RequestParam("code") String code) throws ApiException {

        return ResponseEntity.ok(lazadaSystemService.generate_accessToken(code));
    }
    @RequestMapping("/order")
    ResponseEntity<Map> orderhook(@RequestBody Map params) throws ApiException {
        if (params!=null){
            System.out.println(new Gson().toJson(params));
        }
        if (params!=null && params.get("data") !=null){
            Map data = (Map) params.get("data");
            if (data.get("trade_order_id")!=null){
                lazadaOrderService.create((String) data.get("trade_order_id"));
            }
        }
        return ResponseEntity.ok(Map.of("success","đâsd"));
    }
}
