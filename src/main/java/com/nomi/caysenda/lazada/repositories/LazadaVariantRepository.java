package com.nomi.caysenda.lazada.repositories;

import com.nomi.caysenda.lazada.entity.LazadaVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LazadaVariantRepository extends JpaRepository<LazadaVariantEntity,String> {
    List<LazadaVariantEntity> findAllByCategoryId(Integer categoryId);
    @Query("SELECT v FROM LazadaVariantEntity v LEFT JOIN v.lazadaProductEntity p WHERE p.item_id=:itemId")
    List<LazadaVariantEntity> findAllByProductItemId(@Param("itemId") String id);
}
