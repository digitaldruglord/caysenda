package com.nomi.caysenda.ghtk;


import com.nomi.caysenda.ghtk.models.OrderRequestModelGHTK;
import com.nomi.caysenda.ghtk.models.OrderResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class GHTK {
    private static RestTemplate restTemplate = new RestTemplate();
    public static ResponseEntity<OrderResponse> createOrder(OrderRequestModelGHTK modelGHTK, HttpHeaders headers) {
        HttpEntity<OrderRequestModelGHTK> entity = new HttpEntity<>(modelGHTK, headers);
        ResponseEntity<OrderResponse> responseEntity = restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/shipment/order",
                HttpMethod.POST,
                entity,
                OrderResponse.class
        );
        return responseEntity;
    }

    public static ResponseEntity<Map> getFeeShip(MultiValueMap<String, String> valuedMap,HttpHeaders headers) {
        HttpEntity entity = new HttpEntity<>(headers);
        StringBuilder url = new StringBuilder("https://services.giaohangtietkiem.vn/services/shipment/fee?");
        url.append(valuedMap
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue().get(0))
                .collect(Collectors.joining("&")));
        return restTemplate.exchange(
                url.toString(),
                HttpMethod.POST,
                entity,
                Map.class
        );
    }
    public static ResponseEntity<Map> getStatusOrder(String id,HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/shipment/v2/"+id,
                HttpMethod.POST,
                entity,
                Map.class
        );
    }

    public static ResponseEntity<Map> cancelOrder(String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", "6df4892cF954b8A37EbaA83Db49A80459c89647a");
        headers.add("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/shipment/cancel/" + id,
                HttpMethod.POST,
                entity,
                Map.class
        );
    }

    public static ResponseEntity<Map> cancelOrderByPartnerID(String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token", "6df4892cF954b8A37EbaA83Db49A80459c89647a");
        headers.add("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/shipment/cancel/partner_id:" + id,
                HttpMethod.POST,
                entity,
                Map.class
        );
    }

    public static ResponseEntity<byte[]> printOrderLabel(String id,HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/label/" + id,
                HttpMethod.GET,
                entity,
                byte[].class
        );
    }
    public static ResponseEntity<Map> list_pick(HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "https://services.giaohangtietkiem.vn/services/shipment/list_pick_add",
                HttpMethod.GET,
                entity,
                Map.class
        );
    }
    public static Map<String,String> getPickInfoDefault(){
        Map<String,String> map  = new HashMap<>();
        map.put("pick_province","TP. Hồ Chí Minh");
        map.put("pick_district","Quận 11");
        map.put("pick_ward","Phường 11");
        map.put("pick_address","46/15 cư xá lữ gia");
        map.put("pick_tel","0971806636");
        map.put("pick_name","BH Ngọc Minh");
        map.put("province","TP. Hồ Chí Minh");
        map.put("district","Quận Tân Bình");
        map.put("address","17A Cộng Hòa");
        return map;
    }
    public static Map<String,String> getWorkShift(){
        Map<String,String> map = new HashMap<>();
        map.put("3","Buổi tối");
        map.put("1","Buổi sáng");
        map.put("2","Buổi trưa");

        return map;
    }

    public static Map<String,String> getUserPay(){
        Map<String,String> map = new HashMap<>();
        map.put("1","Shop trả");
        map.put("0","Khách trả");
        return map;
    }
    public static Map<String,String> getTranport(){
        Map<String,String> map = new HashMap<>();
        map.put("road","Đường bộ");
        map.put("fly","Đường bay");
        return map;
    }
}
