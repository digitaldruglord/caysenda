package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.cart.CartStatictis;
import com.nomi.caysenda.dto.cart.CartSummary;
import com.nomi.caysenda.entity.CartEntity;
import com.nomi.caysenda.entity.CartVariantEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.repositories.custom.CartCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity,Integer>, CartCustomRepository {
    CartEntity findByUserCart_Id(Integer userId);
    @Query("SELECT c FROM CartEntity c LEFT JOIN c.cartProvider cp LEFT JOIN c.userCart u WHERE u.id=:userId AND cp.host=:host")
    CartEntity findByUserCart_IdAndDomain(@Param("userId") Integer userId, @Param("host") String host);
    @Query("SELECT CASE WHEN count (c)> 0 THEN true ELSE false END FROM CartEntity c LEFT JOIN c.cartProvider cp LEFT JOIN c.userCart u WHERE u.id=:userId AND cp.host=:host")
    boolean existsByUserCart_IdAndDomain(@Param("userId") Integer userId, @Param("host") String host);
    boolean existsByUserCart_Id(Integer userId);
    @Query("SELECT SUM(vs.quantity) FROM CartEntity c JOIN c.userCart u JOIN c.products ps JOIN ps.productCartEntity p JOIN ps.variants vs WHERE u.id=?1 AND p.id=?2")
    Long countByProduct(Integer userId,Integer productId);
    @Query("SELECT SUM(vs.quantity) FROM CartEntity c JOIN c.userCart u JOIN c.products ps LEFT JOIN c.cartProvider cp JOIN ps.productCartEntity p JOIN ps.variants vs WHERE u.id=?1 AND p.id=?2 AND cp.host=?3")
    Long countByProductAndDomain(Integer userId,Integer productId,String host);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartSummary(c.id,u.username,SUM(v.cartVariantEntity.cost*v.quantity),c.note,u.id,(c.createDate),(c.modifiedDate)) FROM CartEntity c LEFT JOIN c.userCart u LEFT JOIN c.products p LEFT JOIN p.variants v GROUP BY c ORDER BY SUM(v.cartVariantEntity.cost*v.quantity) DESC ")
    Page<CartSummary> findAllOrderByCost(Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartSummary(c.id,u.username,SUM(v.cartVariantEntity.cost*v.quantity) ,c.note,u.id,(c.createDate) ,(c.modifiedDate)) FROM CartEntity c LEFT JOIN c.userCart u LEFT JOIN c.products p LEFT JOIN p.variants v GROUP BY c ORDER BY c.createDate DESC ")
    Page<CartSummary> findAllOrderByDate(Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.cart.CartSummary(c.id,u.username,SUM(v.cartVariantEntity.cost*v.quantity),c.note,u.id,c.createDate,c.modifiedDate) FROM CartEntity c LEFT JOIN c.userCart u LEFT JOIN c.products p LEFT JOIN p.variants v WHERE c.id=:id GROUP BY c")
    CartSummary findAllOrderById(@Param("id") Integer id);
}
