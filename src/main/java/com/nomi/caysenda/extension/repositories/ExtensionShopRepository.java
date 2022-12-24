package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.extension.dto.ShopExtensionDTO;
import com.nomi.caysenda.extension.entity.ExtensionShopEntity;
import com.nomi.caysenda.extension.repositories.custom.CustomExtensionShopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtensionShopRepository extends JpaRepository<ExtensionShopEntity,Integer>, CustomExtensionShopRepository {
    ExtensionShopEntity findByLink(String link);
    Page<ExtensionShopEntity> findAllByUserExtensionShop_Id(Integer userId, Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.extension.dto.ShopExtensionDTO(s.id,s.name,s.link,s.sku,s.exchangeRate,s.factorDefault,s.factor1,s.factor2,s.factor3,s.factor4,s.mainProduct,s.status,u,COUNT (p.id)) FROM ExtensionShopEntity s LEFT JOIN s.products p LEFT JOIN s.userExtensionShop u GROUP BY s.id")
    Page<ShopExtensionDTO> findAllShopExtensionDTO(Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.extension.dto.ShopExtensionDTO(s.id,s.name,s.link,s.sku,s.exchangeRate,s.factorDefault,s.factor1,s.factor2,s.factor3,s.factor4,s.mainProduct,s.status,u,COUNT (p.id)) FROM ExtensionShopEntity s LEFT JOIN s.products p LEFT JOIN s.userExtensionShop u WHERE u.id=:userId GROUP BY s.id")
    Page<ShopExtensionDTO> findAllShopExtensionDTOByUserId(@Param("userId") Integer userId,Pageable pageable);
}
