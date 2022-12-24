package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.extension.entity.ExtensionVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtensionVariantRepository extends JpaRepository<ExtensionVariantEntity,Integer> {
    List<ExtensionVariantEntity> findAllByProduct_Id(Integer productId);
    ExtensionVariantEntity findBySku(String sku);
    @Query("SELECT v FROM ExtensionVariantEntity  v LEFT JOIN v.product p LEFT JOIN p.shop s WHERE s.id=:shopId")
    List<ExtensionVariantEntity> findAllByShop(@Param("shopId") Integer shopId);
    Boolean existsBySku(String sku);
    @Query("SELECT v FROM ExtensionVariantEntity v LEFT JOIN v.product p WHERE p.id=:productId AND v.parent=:parent")
    List<ExtensionVariantEntity> findAllByProductIdAndParent(Integer productId,String parent);
}
