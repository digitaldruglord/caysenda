package com.nomi.caysenda.lazada.repositories;

import com.nomi.caysenda.lazada.entity.LazadaOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LazadaOrderRepository extends JpaRepository<LazadaOrderEntity,Integer> {
    LazadaOrderEntity findByOrderId(Integer orderId);
    LazadaOrderEntity findByLazadaId(String lazadaId);
    Boolean existsByLazadaId(String lazadaId);
}
