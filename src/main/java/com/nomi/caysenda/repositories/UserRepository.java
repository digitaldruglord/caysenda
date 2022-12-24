package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.RoleEntity;
import com.nomi.caysenda.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
;import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
        UserEntity findByUsernameOrEmail(String userName,String email);
        UserEntity findByUsernameOrEmailOrPhonenumber(String userName,String email,String phoneNumber);
        List<UserEntity> findAllByRolesContains(RoleEntity roleEntity);
        Boolean existsByPhonenumber(String phone);
        Boolean existsByPhonenumberAndIdNot(String phone,Integer userId);
        Boolean existsByEmail(String email);
        Boolean existsByUsername(String username);
        Boolean existsByUsernameOrEmailOrPhonenumber(String username,String email,String phone);
        Page<UserEntity> findAllByUsernameLikeOrPhonenumberLikeOrEmailLike(String username,String phone,String email,Pageable pageable);
    }
