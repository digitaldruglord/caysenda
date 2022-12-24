package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.TrackingOrderDTO;
import com.nomi.caysenda.entity.TrackingOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TrackingOrderRepository extends JpaRepository<TrackingOrderEntity,Integer> {
    List<TrackingOrderEntity> findAllByOrderTracking_Id(Integer orderId);
    @Query("SELECT t FROM TrackingOrderEntity t WHERE ((t.statusZh!=:status) OR (t.statusZh IS NULL)) AND t.createDate >= :fromDate")
    List<TrackingOrderEntity> findAllByStatusZhNot(@Param("status") String status,
                                                   @Param("fromDate") Date from);
    List<TrackingOrderEntity> findAllByOrOrderCode(String orderCode);
    @Query("SELECT new com.nomi.caysenda.dto.TrackingOrderDTO(t.id,t.name,t.orderCode,t.status,t.statusZh,o.id,t.receiptDate,t.received) FROM TrackingOrderEntity t LEFT JOIN t.orderTracking o WHERE o.id IN :ids")
    List<TrackingOrderDTO> findAllByIds(List<Integer> ids);
    List<TrackingOrderEntity> findAllByCreateDateBetween(Date from,Date to);

}
