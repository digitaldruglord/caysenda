package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    RoleEntity findByCode(String code);
    @Query("SELECT r FROM RoleEntity r JOIN r.users u WHERE u.id=?1")
    List<RoleEntity> findByUserId(Integer userId);
}
