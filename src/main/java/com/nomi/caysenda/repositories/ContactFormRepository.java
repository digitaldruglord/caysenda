package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.ContactFormDeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactFormRepository extends JpaRepository<ContactFormDeliveryEntity,Integer> {
}
