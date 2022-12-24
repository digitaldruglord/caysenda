package com.nomi.caysenda.repositories.delivery;

import com.nomi.caysenda.entity.delivery.DeliveryCurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryCurrencyRepository extends JpaRepository<DeliveryCurrencyEntity,Integer> {
    Page<DeliveryCurrencyEntity> findAllByUserDeliveryEntity_Id(Integer userId,Pageable pageable);
    DeliveryCurrencyEntity findByIdAndUserDeliveryEntity_Id(Integer id,Integer userId);
    Long countByStatus(String status);
}
