package com.nomi.caysenda.api.admin.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.api.admin.AdminAddressAPI;
import com.nomi.caysenda.entity.AddressDictrictEntity;
import com.nomi.caysenda.entity.AddressProviceEntity;
import com.nomi.caysenda.entity.AddressWardsEntity;
import com.nomi.caysenda.repositories.AddressDictrictRepository;
import com.nomi.caysenda.repositories.AddressProvinceRepository;
import com.nomi.caysenda.repositories.AddressWardRepository;
import com.nomi.caysenda.utils.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Controller
@RequestMapping("/api/admin/address")
public class AdminAddressAPIImpl implements AdminAddressAPI {
    @Autowired
    AddressProvinceRepository provinceRepository;
    @Autowired
    AddressDictrictRepository dictrictRepository;
    @Autowired
    AddressWardRepository wardRepository;

    @Override
    public ResponseEntity<Map> autoImportProvince() throws IOException {

        File s = UploadFileUtils.getPath("addressdata/");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> car = objectMapper.readValue(new File(s.getPath()+"cities.json"), Map.class);
        car.entrySet().forEach(stringStringEntry -> {
            AddressProviceEntity proviceEntity = new AddressProviceEntity();
            proviceEntity.setId(stringStringEntry.getKey());
            proviceEntity.setName(stringStringEntry.getValue());
            provinceRepository.save(proviceEntity);
        });

        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> autoImportDictricts() throws IOException {
        File s = UploadFileUtils.getPath("addressdata/");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Map<String,String>> car = objectMapper.readValue(new File(s.getPath()+"districts.json"), Map.class);
        car.entrySet().forEach(stringStringEntry -> {
            AddressProviceEntity proviceEntity = provinceRepository.findById(stringStringEntry.getKey()).get();
            stringStringEntry.getValue().entrySet().forEach(stringStringEntry1 -> {
                AddressDictrictEntity dictrictEntity = new AddressDictrictEntity();
                dictrictEntity.setId(stringStringEntry1.getKey());
                dictrictEntity.setProviceEntity(proviceEntity);
                dictrictEntity.setName(stringStringEntry1.getValue());
                dictrictRepository.save(dictrictEntity);
            });
        });

        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> autoImportWards() throws IOException {
        File s = UploadFileUtils.getPath("addressdata/");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Map<String,String>> car = objectMapper.readValue(new File(s.getPath()+"wards.json"), Map.class);
        car.entrySet().forEach(stringStringEntry -> {
            AddressDictrictEntity dictrictEntity = dictrictRepository.findById(stringStringEntry.getKey()).get();
            stringStringEntry.getValue().entrySet().forEach(stringStringEntry1 -> {
                AddressWardsEntity wardsEntity = new AddressWardsEntity();
                wardsEntity.setId(stringStringEntry1.getKey());
                wardsEntity.setDictrictEntity(dictrictEntity);
                wardsEntity.setName(stringStringEntry1.getValue());
                wardRepository.save(wardsEntity);
            });
        });
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getProvinces() throws IOException {
        return ResponseEntity.ok(Map.of("data",provinceRepository.findAll()));
    }

    @Override
    public ResponseEntity<Map> getDictricts(String provinceId) throws IOException {
        return ResponseEntity.ok(Map.of("data",dictrictRepository.findAllByProviceEntity_Id(provinceId)));
    }

    @Override
    public ResponseEntity<Map> getWards(String dictrictId) throws IOException {
        return ResponseEntity.ok(Map.of("data",wardRepository.findAllByDictrictEntity_Id(dictrictId)));
    }
}
