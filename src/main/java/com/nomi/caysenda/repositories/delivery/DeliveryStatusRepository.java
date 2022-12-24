package com.nomi.caysenda.repositories.delivery;

import com.nomi.caysenda.entity.delivery.DeliveryStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatusEntity,Integer> {
}
