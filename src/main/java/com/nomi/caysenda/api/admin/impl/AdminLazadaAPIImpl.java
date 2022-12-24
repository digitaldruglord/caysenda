package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminLazadaAPI;
import com.nomi.caysenda.lazada.services.LazadaProductService;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.options.model.LazadaSetting;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/admin/lazada")
public class AdminLazadaAPIImpl implements AdminLazadaAPI {
    @Autowired
    SettingService settingService;
    @Autowired
    LazadaSystemService lazadaSystemService;
    @Autowired
    LazadaProductService lazadaProductService;
    @Override
    public ResponseEntity<Map> getData() {

        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getLazadaSetting()));
    }

    @Override
    public ResponseEntity<Map> updateData(LazadaSetting lazadaSetting) {
        settingService.updateLazadaSetting(lazadaSetting);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getToken(String code) throws ApiException {

        return ResponseEntity.ok(Map.of("success",true,"data",lazadaSystemService.generate_accessToken(code)));
    }

    @Override
    public ResponseEntity<Map> updateToken(String token) {
        return null;
    }

    @Override
    public ResponseEntity<Map> getCategories() throws ApiException {

        return ResponseEntity.ok(Map.of("success",true,"data", lazadaProductService.getCategoryTree()));
    }

    @Override
    public ResponseEntity<Map> uptoLazada(Integer categoryId, Integer lazadaCategoryId) throws Exception {
        lazadaProductService.createByCategoryId(categoryId,lazadaCategoryId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> removeProduct(Integer categoryId, String sku) throws Exception {
        if (categoryId!=null){
            lazadaProductService.removeProductByCatId(categoryId);
        }
        if (sku!=null){

        }

        return ResponseEntity.ok(Map.of("success",true));
    }

}
