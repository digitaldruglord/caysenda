package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.delivery.UserDeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeliveryRepository extends JpaRepository<UserDeliveryEntity,Integer> {
    UserDeliveryEntity findByUserName(String userName);
}
