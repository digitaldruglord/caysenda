package com.nomi.caysenda.repositories.delivery;

import com.nomi.caysenda.entity.delivery.DeliveryOrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrderEntity,Integer> {
    List<DeliveryOrderEntity> findAllByUserDeliveryEntity_Id(Integer userId,Pageable pageable);
    DeliveryOrderEntity findByIdAndUserDeliveryEntity_Id(Integer id, Integer userId);
    @Query("SELECT COUNT(d.id) FROM DeliveryOrderEntity d JOIN d.statusList s GROUP BY d.id HAVING COUNT (d.id)=1")
    List<Long> deliveryOrderCount();
}
