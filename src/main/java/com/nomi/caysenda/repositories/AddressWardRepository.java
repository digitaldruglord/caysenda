package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.AddressWardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressWardRepository  extends JpaRepository<AddressWardsEntity,String> {
    List<AddressWardsEntity> findAllByDictrictEntity_Id(String dictrictId);
}
