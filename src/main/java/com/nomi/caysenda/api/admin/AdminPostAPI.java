package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.post.AdminPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface AdminPostAPI {
    @GetMapping("")
    ResponseEntity<Map> posts(@RequestParam(value = "page",required = false) Integer page,
                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
                              @RequestParam(value = "type",required = false) String type);
    @DeleteMapping()
    ResponseEntity<Map> deleteById(@RequestParam("id")Integer id);
    @GetMapping("/findbyid")
    ResponseEntity<Map> findById(@RequestParam(value = "id",required = false) Integer id);
    @PostMapping("")
    ResponseEntity<Map> createAndUpdate(@RequestBody AdminPostRequest postRequest);
}
