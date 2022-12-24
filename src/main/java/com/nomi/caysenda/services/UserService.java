package com.nomi.caysenda.services;



import com.nomi.caysenda.api.admin.model.user.request.AdminUserRegisterRequest;
import com.nomi.caysenda.api.admin.model.user.responses.AdminUserRegisterResponse;
import com.nomi.caysenda.controller.requests.UserRegisterRequest;
import com.nomi.caysenda.controller.requests.user.UserUpdateRequest;
import com.nomi.caysenda.controller.responses.UserRegisterResponse;
import com.nomi.caysenda.controller.responses.user.UserChangePasswordResponse;
import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.exceptions.user.UserLoginException;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findById(Integer userId);
    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> search(String keyword,Pageable pageable);
    void update(UserUpdateRequest updateRequest) throws UserRegisterException;
    Map login(String userName, String password) throws UserLoginException;
    Map register(UserDTO userDTO) throws UserRegisterException;
    UserRegisterResponse register(UserRegisterRequest request) throws UserRegisterException;
    AdminUserRegisterResponse register(AdminUserRegisterRequest userDTO) throws UserRegisterException;
    AdminUserRegisterResponse update(AdminUserRegisterRequest request);
    UserDTO findByUserNameOrEmail(String param);
    List<UserEntity> findAllByRole(String role);
    UserChangePasswordResponse changePassword(String passwordOld,String passwordNew,String passwordNewConfirm);
    UserChangePasswordResponse newPassword(Integer userId,String passwordNew,String passwordNewConfirm);
    void sendEmailForgotPassword(String userName);
    void generateExcel() throws IOException;

}
