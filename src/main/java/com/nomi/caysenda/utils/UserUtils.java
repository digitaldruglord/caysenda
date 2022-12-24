package com.nomi.caysenda.utils;


import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.UserEntity;

public class UserUtils {
    public static UserDTO convertEntitytoDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setId(userEntity.getId());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setPhonenumber(userEntity.getPhonenumber());
        return userDTO;
    }
    public static UserEntity convertEntitytoDTO(UserDTO userDTO){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPhonenumber(userDTO.getPhonenumber());
        userEntity.setStatus(userDTO.getStatus());
        userEntity.setPassword(userDTO.getPassword());
        return userEntity;
    }

}
