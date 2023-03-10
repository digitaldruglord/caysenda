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
        map.put("pick_province","TP. H??? Ch?? Minh");
        map.put("pick_district","Qu???n 11");
        map.put("pick_ward","Ph?????ng 11");
        map.put("pick_address","46/15 c?? x?? l??? gia");
        map.put("pick_tel","0971806636");
        map.put("pick_name","BH Ng???c Minh");
        map.put("province","TP. H??? Ch?? Minh");
        map.put("district","Qu???n T??n B??nh");
        map.put("address","17A C???ng H??a");
        return map;
    }
    public static Map<String,String> getWorkShift(){
        Map<String,String> map = new HashMap<>();
        map.put("3","Bu???i t???i");
        map.put("1","Bu???i s??ng");
        map.put("2","Bu???i tr??a");

        return map;
    }

    public static Map<String,String> getUserPay(){
        Map<String,String> map = new HashMap<>();
        map.put("1","Shop tr???");
        map.put("0","Kh??ch tr???");
        return map;
    }
    public static Map<String,String> getTranport(){
        Map<String,String> map = new HashMap<>();
        map.put("road","???????ng b???");
        map.put("fly","???????ng bay");
        return map;
    }
}
