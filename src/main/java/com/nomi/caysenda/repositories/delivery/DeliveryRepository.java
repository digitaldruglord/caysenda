package com.nomi.caysenda.repositories.delivery;

import com.nomi.caysenda.entity.delivery.DeliveryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity,Integer> {
    DeliveryEntity findByIdAndUserDeliveryEntity_Id(Integer id,Integer userId);
    Page<DeliveryEntity> findAllByUserDeliveryEntity_Id(Integer userId, Pageable pageable);
    @Query("SELECT COUNT(d.id) FROM DeliveryEntity d JOIN d.statusList s GROUP BY d.id HAVING COUNT (d.id)=1")
    List<Long> deliveryCount();
}
