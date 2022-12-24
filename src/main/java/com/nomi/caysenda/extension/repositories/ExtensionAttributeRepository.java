package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.extension.entity.ExtensionAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtensionAttributeRepository extends JpaRepository<ExtensionAttributeEntity,Integer> {
    List<ExtensionAttributeEntity> findAllByProductAttribute_Id(Integer productId);
}
