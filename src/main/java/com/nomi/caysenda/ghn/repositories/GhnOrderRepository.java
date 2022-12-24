package com.nomi.caysenda.ghn.repositories;

import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.model.responses.OrderSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GhnOrderRepository extends JpaRepository<GHNOrderEntity,Integer> {
    GHNOrderEntity findByOrderCodeGhn(String orderCode);
    List<GHNOrderEntity> findAllByOrderId(Integer orderId, Sort sort);
    Boolean existsByOrderId(Integer orderId);
    Boolean existsByOrderCodeGhn(String order_code);
    Boolean existsByOrderCode(String order_code);
    @Query("SELECT new com.nomi.caysenda.ghn.model.responses.OrderSummary(o.id,o.status,o.orderCodeGhn,o.orderCode,o.amount,o.cod,o.totalFee,o.productName,o.modifiedDate) FROM GHNOrderEntity o WHERE o.orderId=:orderId")
    List<OrderSummary> findAllByOrderId(@Param("orderId") Integer orderId);
    @Query("SELECT o FROM GHNOrderEntity o WHERE o.orderId IN :ids")
    List<GHNOrderEntity> findAllByOrderId(@Param("ids") List<Integer> ids);
}
