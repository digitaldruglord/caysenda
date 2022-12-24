package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.partner.PartnerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface AdminPartnerAPI {
    @GetMapping()
    ResponseEntity<Map> findAll();
    @PostMapping()
    ResponseEntity<Map> save(@RequestBody PartnerRequest partnerRequest);
    @DeleteMapping()
    ResponseEntity<Map> delete(@RequestParam("id") Integer id);
}
