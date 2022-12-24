package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.extension.dto.ExtensionOrderDTO;
import com.nomi.caysenda.extension.entity.ExtensionOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExtensionOrderRepository extends JpaRepository<ExtensionOrderEntity,Integer> {
    @Query("SELECT new com.nomi.caysenda.extension.dto.ExtensionOrderDTO(o.id,o.orderId,o.status,o.fullName,o.phoneNumber) FROM ExtensionOrderEntity o")
    Page<ExtensionOrderDTO> findAllForDTO(Pageable pageable);
    List<ExtensionOrderEntity> findByOrderId(Integer orderId);
    ExtensionOrderEntity findByOrderIdAndSub(Integer orderId,Boolean sub);
}
