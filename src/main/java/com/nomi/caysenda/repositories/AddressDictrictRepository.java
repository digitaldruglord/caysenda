package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.AddressDictrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDictrictRepository extends JpaRepository<AddressDictrictEntity,String> {
    List<AddressDictrictEntity> findAllByProviceEntity_Id(String provinceId);
}
