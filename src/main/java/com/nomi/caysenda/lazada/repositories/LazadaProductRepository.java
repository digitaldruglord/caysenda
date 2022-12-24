package com.nomi.caysenda.lazada.repositories;

import com.nomi.caysenda.lazada.entity.LazadaProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LazadaProductRepository extends JpaRepository<LazadaProductEntity,String> {
    LazadaProductEntity findByProductSku(String sku);
    List<LazadaProductEntity> findAllByCategoryId(Integer category);
}
