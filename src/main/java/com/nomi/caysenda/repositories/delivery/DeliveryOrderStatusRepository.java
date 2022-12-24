package com.nomi.caysenda.repositories.delivery;

import com.nomi.caysenda.entity.delivery.DeliveryOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOrderStatusRepository extends JpaRepository<DeliveryOrderStatus,Integer> {
}
