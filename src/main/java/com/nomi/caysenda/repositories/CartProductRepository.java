package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.dto.cart.CartProductDTO;
import com.nomi.caysenda.entity.CartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProductEntity,Long> {
    boolean existsByProductCartEntity_IdAndCartProduct_Id(Integer productId,Integer cartId);
    CartProductEntity findByProductCartEntity_IdAndCartProduct_Id(Integer productId,Integer cartId);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartProductDTO(pc.id,p.id,p.nameVi,p.skuVi,p.slugVi,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.thumbnail,p.categoryDefault,pc.isRetailt,p.link,p.nameZh,p.unit,p.topFlag) FROM CartProductEntity pc LEFT JOIN pc.productCartEntity p  LEFT JOIN pc.cartProduct c LEFT JOIN p.categories categories WHERE c.id=?1 AND categories.id=p.categoryDefault ORDER BY pc.modifyDate DESC, pc.createDate DESC")
    List<CartProductDTO> findForCartDTO(Integer cartId);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartProductDTO(pc.id,p.id,p.nameVi,p.skuVi,p.slugVi,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.thumbnail,p.categoryDefault,pc.isRetailt,p.link,p.nameZh,p.unit,p.topFlag) FROM CartProductEntity pc LEFT JOIN pc.productCartEntity p  LEFT JOIN pc.cartProduct c LEFT JOIN p.categories categories LEFT JOIN c.userCart user WHERE user.id=?1 AND categories.id=?2 ORDER BY pc.modifyDate DESC, pc.createDate DESC")
    List<CartProductDTO> findForCartDTO(Integer userId,Integer catId);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartProductDTO(pc.id,p.id,p.nameVi,p.skuVi,p.slugVi,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.thumbnail,p.categoryDefault,pc.isRetailt,p.link,p.nameZh,p.unit,p.topFlag) FROM CartProductEntity pc LEFT JOIN pc.productCartEntity p LEFT JOIN p.categories categories WHERE pc.id=?1 AND categories.id=p.categoryDefault ORDER BY pc.modifyDate DESC, pc.createDate DESC")
    CartProductDTO findForCartDTOById(Long productId);
    @Query("SELECT p FROM CartProductEntity p LEFT JOIN p.variants vc GROUP BY p HAVING COUNT(vc)<=0")
    List findAllByVariantsLessThanZero();
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartProductDTO(pc.id,p.id,p.nameVi,p.skuVi,p.slugVi,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.thumbnail,p.categoryDefault,pc.isRetailt,p.link,p.nameZh,p.unit,p.topFlag) FROM CartProductEntity pc LEFT JOIN pc.productCartEntity p LEFT JOIN pc.cartProduct c LEFT JOIN c.userCart u LEFT JOIN p.categories categories WHERE categories.id=?1 AND u.id=?2 ORDER BY pc.modifyDate DESC, pc.createDate DESC")
    List<CartProductDTO> findForCartDTOByCat(Integer catId,Integer userId);
    @Query("SELECT SUM (vc.quantity) FROM CartProductEntity pc LEFT JOIN pc.variants vc LEFT JOIN pc.productCartEntity p LEFT JOIN pc.cartProduct c LEFT JOIN c.userCart u  WHERE p.id=?1 AND u.id=?2")
    Long countQuantityByProductId(Integer productId,Integer userId);

}
