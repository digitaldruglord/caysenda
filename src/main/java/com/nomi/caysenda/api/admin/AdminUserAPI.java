package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.user.request.AdminUserRegisterRequest;
import com.nomi.caysenda.api.admin.model.user.responses.AdminUserRegisterResponse;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AdminUserAPI {
    @GetMapping()
    ResponseEntity<Page> users(@RequestParam(value = "page",required = false) Integer page,
                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
                               @RequestParam(value = "keyword",required = false) String keyword);
    @GetMapping("/findbyid")
    ResponseEntity<Map> findByID(@RequestParam(value = "id",required = false) Integer id);
    @GetMapping("/findall-role")
    ResponseEntity<List> findAllRole();
    @PostMapping()
    ResponseEntity<AdminUserRegisterResponse> register(@RequestBody AdminUserRegisterRequest requestParams) throws UserRegisterException;
    @PutMapping()
    ResponseEntity<Map> update(@RequestBody AdminUserRegisterRequest requestParams);
    @DeleteMapping()
    ResponseEntity<Map> delete(@RequestParam("id") Integer id);
    @GetMapping("/export-users")
    ResponseEntity<Map> exportUsers() throws IOException;
}
