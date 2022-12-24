package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminUserAPI;
import com.nomi.caysenda.api.admin.model.user.request.AdminUserRegisterRequest;
import com.nomi.caysenda.api.admin.model.user.responses.AdminUserRegisterResponse;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import com.nomi.caysenda.repositories.RoleRepository;
import com.nomi.caysenda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserAPIImpl implements AdminUserAPI {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Override
    public ResponseEntity<Page> users(Integer page, Integer pageSize, String keyword) {
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?20:pageSize, Sort.by(Sort.Direction.DESC,"id"));
        if (keyword!=null){
            return new ResponseEntity<>(userService.search(keyword,pageable),HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.findAll(pageable),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> findByID(Integer id) {

        UserEntity userEntity = userService.findById(id).orElse(null);
        if (userEntity!=null){
            return ResponseEntity.ok(Map.of("success",true,"data",userEntity));
        }else {
            return ResponseEntity.ok(Map.of("success",false,"data",userEntity));
        }

    }

    @Override
    public ResponseEntity<List> findAllRole() {
      return ResponseEntity.ok(roleRepository.findAll());
    }

    @Override
    public ResponseEntity<AdminUserRegisterResponse> register(AdminUserRegisterRequest requestParams) throws UserRegisterException {
        if (requestParams.getId()!=null){
            return new ResponseEntity<>(userService.update(requestParams), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(userService.register(requestParams), HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<Map> update(AdminUserRegisterRequest requestParams) {

        return null;
    }

    @Override
    public ResponseEntity<Map> delete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Map> exportUsers() throws IOException {
        userService.generateExcel();
        return ResponseEntity.ok(Map.of("success",true));
    }
}
