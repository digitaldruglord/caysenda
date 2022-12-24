package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity,Integer> {
    Page<ImageEntity> findAllByType(String type, Pageable pageable);
    Page<ImageEntity> findAllByTypeAndNameLike(String type,String name,Pageable pageable);
    List<ImageEntity> findAllByNameLike(String name);
    Boolean existsByPath(String path);
    ImageEntity findByPath(String path);
    List<ImageEntity> findAllByProductSKU(String sku);
    List<ImageEntity> findAllByVariantSKU(String sku);
}
