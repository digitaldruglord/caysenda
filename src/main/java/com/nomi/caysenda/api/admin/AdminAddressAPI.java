package com.nomi.caysenda.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface AdminAddressAPI {
    @GetMapping("/import-provinces")
    ResponseEntity<Map> autoImportProvince() throws IOException;
    @GetMapping("/import-dictricts")
    ResponseEntity<Map> autoImportDictricts() throws IOException;
    @GetMapping("/import-wards")
    ResponseEntity<Map> autoImportWards() throws IOException;
    @GetMapping("/get-provinces")
    ResponseEntity<Map> getProvinces() throws IOException;
    @GetMapping("/get-dictricts")
    ResponseEntity<Map> getDictricts(@RequestParam("provinceId") String provinceId) throws IOException;
    @GetMapping("/get-wards")
    ResponseEntity<Map> getWards(@RequestParam("dictrictId") String dictrictId) throws IOException;
}
