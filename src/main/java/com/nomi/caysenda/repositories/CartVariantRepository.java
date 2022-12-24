package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.cart.CartVariantDTO;
import com.nomi.caysenda.entity.CartVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CartVariantRepository extends JpaRepository<CartVariantEntity,Long> {
    boolean existsByProduct_IdAndAndCartVariantEntity_Id(Long productId,Integer variantId);
    CartVariantEntity findByProduct_IdAndAndCartVariantEntity_Id(Long productId,Integer variantId);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartVariantDTO(vc.id,v.nameVi,v.thumbnail,v.skuVi,v.price,vc.quantity,v.price,v.vip1,v.vip2,v.vip3,v.vip4,v.cost,vc.active,v.id,v.weight,v.nameZh,vc.groupName,vc.groupZhName,vc.groupSku) FROM CartVariantEntity vc LEFT JOIN vc.product pc LEFT JOIN vc.cartVariantEntity v WHERE pc.id=?1 ")
    List<CartVariantDTO> findAllByProductIdForCartVariantDTO(Long productId);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartVariantDTO(vc.id,v.nameVi,v.thumbnail,v.skuVi,v.price,vc.quantity,v.price,v.vip1,v.vip2,v.vip3,v.vip4,v.cost,vc.active,v.id,v.weight,v.nameZh,vc.groupName,vc.groupZhName,vc.groupSku) FROM CartVariantEntity vc LEFT JOIN vc.cartVariantEntity v WHERE vc.id=?1 ")
    CartVariantDTO findByIdForCartVariantDTO(Long variantId);
    @Query("SELECT vc FROM CartVariantEntity vc LEFT JOIN vc.product pc LEFT JOIN pc.productCartEntity p WHERE p.categoryDefault=?1")
    List<CartVariantEntity> findByCatId(Integer catId);
    List<CartVariantEntity> findByProduct_Id(Long productId);
    @Transactional
    void deleteByActiveAndProduct_CartProduct_UserCart_Id(Boolean active,Integer userId);
    @Query("SELECT vc FROM CartVariantEntity vc LEFT JOIN vc.product pc LEFT JOIN pc.cartProduct cart LEFT JOIN cart.userCart user WHERE user.id=?1")
    List<CartVariantEntity> findAllByUserId(Integer userId);
}
