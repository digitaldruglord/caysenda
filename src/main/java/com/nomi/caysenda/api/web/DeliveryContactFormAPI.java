package com.nomi.caysenda.api.web;

import com.nomi.caysenda.api.web.models.ContactFormRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface DeliveryContactFormAPI {
    @PostMapping("")
    ResponseEntity<Map> contactForm(@RequestBody ContactFormRequest formRequest);
}
