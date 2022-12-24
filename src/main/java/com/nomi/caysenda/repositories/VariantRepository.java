package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.ProductAndVariantDTO;
import com.nomi.caysenda.entity.VariantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VariantRepository extends JpaRepository<VariantEntity,Integer> {
    VariantEntity findBySkuVi(String skuVi);
    @Query("SELECT new com.nomi.caysenda.dto.ProductAndVariantDTO(v.id,p.id,p.categoryDefault,v.nameVi,p.nameVi,v.thumbnail,p.thumbnail,v.price,v.vip1,v.vip2,v.vip3,v.vip4,v.stock,p.unit,p.link,p.slugVi) FROM VariantEntity v LEFT JOIN v.productEntity p WHERE p.nameVi LIKE CONCAT('%',:keyword,'%')")
    Page<ProductAndVariantDTO> findAllForProductAndVariantDTOByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.ProductAndVariantDTO(v.id,p.id,p.categoryDefault,v.nameVi,p.nameVi,v.thumbnail,p.thumbnail,v.price,v.vip1,v.vip2,v.vip3,v.vip4,v.stock,p.unit,p.link,p.slugVi) FROM VariantEntity v LEFT JOIN v.productEntity p")
    Page<ProductAndVariantDTO> findAllForProductAndVariantDTO(Pageable pageable);
}
