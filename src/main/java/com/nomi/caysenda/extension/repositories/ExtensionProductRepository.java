package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.extension.entity.ExtensionProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExtensionProductRepository extends JpaRepository<ExtensionProductEntity,Integer> {
    Page<ExtensionProductEntity> findAllByNameZhLikeOrStandardNameLike(String name,String standarName,Pageable pageable);
    ExtensionProductEntity findBySku(String sku);
    ExtensionProductEntity findByLink(String link);
    List<ExtensionProductEntity> findAllByLink(String link);
    List<ExtensionProductEntity> findAllByShop_Id(Integer shopId);
    Page<ExtensionProductEntity> findAllByShop_Id(Integer shopId, Pageable pageable);
    Page<ExtensionProductEntity> findAllByShop_IdAndNameZhLikeOrStandardNameLike(Integer shopId,String nameZh,String standarName, Pageable pageable);
    Page<ExtensionProductEntity> findAllByShop_UserExtensionShop_Id(Integer userId,Pageable pageable);
    Page<ExtensionProductEntity> findAllByShop_UserExtensionShop_IdAndNameZhLike(Integer userId,String nameZh,Pageable pageable);
    Page<ExtensionProductEntity> findAllByShop_IdAndShop_UserExtensionShop_Id(Integer shopId,Integer userId,Pageable pageable);
    Boolean existsByLink(String link);
    Boolean existsBySku(String sku);
    ExtensionProductEntity findByIdAndShop_UserExtensionShop_Id(Integer id,Integer userId);
    @Query("SELECT p FROM ExtensionProductEntity p GROUP BY p.link having COUNT (link)>1")
    List<ExtensionProductEntity> findAllByCount();

}
