package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.OrderAdminDTO;
import com.nomi.caysenda.dto.OrderDTO;
import com.nomi.caysenda.dto.OrderWithTrackingDTO;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.TrackingOrderEntity;
import com.nomi.caysenda.repositories.custom.OrderCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;


public interface OrderRepository extends JpaRepository<OrderEntity,Integer>, OrderCustomRepository {
    @Query("SELECT new com.nomi.caysenda.dto.OrderAdminDTO(o.id,o.billingFullName,o.billingPhoneNumber,o.status,o.method,o.productAmount,o.orderAmount,o.ship,SUM(d.cost*d.quantity),o.billingEmail,o.billingAddress,o.billingCity,o.billingDistrict,o.billingWards,o.createDate,o.incurredCost,o.paid,pr.providerName,o.orderCode,o.note,o.adminNote) FROM OrderEntity o JOIN o.detailts d LEFT JOIN o.providerOrder pr WHERE  (o.billingFullName LIKE :keyword OR o.billingEmail LIKE :keyword OR o.billingPhoneNumber LIKE :keyword) AND o.trash=:trash GROUP BY o")
    Page<OrderAdminDTO> findAllForOrderAdminDTO(@Param("keyword")String keyword,
                                                @Param("trash") Boolean trash, Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.OrderAdminDTO(o.id,o.billingFullName,o.billingPhoneNumber,o.status,o.method,o.productAmount,o.orderAmount,o.ship,SUM(d.cost*d.quantity),o.billingEmail,o.billingAddress,o.billingCity,o.billingDistrict,o.billingWards,o.createDate,o.incurredCost,o.paid,pr.providerName,o.orderCode,o.note,o.adminNote) FROM OrderEntity o JOIN o.detailts d LEFT JOIN o.providerOrder pr  WHERE (o.billingFullName LIKE :keyword OR o.billingEmail LIKE :keyword OR o.billingPhoneNumber LIKE :keyword) AND o.trash=:trash AND o.status=:status GROUP BY o")
    Page<OrderAdminDTO> findAllForOrderAdminDTOByStatus(@Param("keyword")String keyword,
                                                        @Param("trash") Boolean trash,
                                                        @Param("status") String status,Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.OrderAdminDTO(o.id,o.billingFullName,o.billingPhoneNumber,o.status,o.method,o.productAmount,o.orderAmount,o.ship,SUM(d.cost*d.quantity),o.billingEmail,o.billingAddress,o.billingCity,o.billingDistrict,o.billingWards,o.createDate,o.incurredCost,o.paid,pr.providerName,o.orderCode,o.note,o.adminNote) FROM OrderEntity o JOIN o.detailts d LEFT JOIN o.providerOrder pr WHERE  (o.billingFullName LIKE :keyword OR o.billingEmail LIKE :keyword OR o.billingPhoneNumber LIKE :keyword) AND o.trash=:trash AND pr.id=:host GROUP BY o")
    Page<OrderAdminDTO> findAllForOrderAdminDTO(@Param("keyword")String keyword,
                                                @Param("trash") Boolean trash,
                                                @Param("host") Integer host,
                                                Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.OrderAdminDTO(o.id,o.billingFullName,o.billingPhoneNumber,o.status,o.method,o.productAmount,o.orderAmount,o.ship,SUM(d.cost*d.quantity),o.billingEmail,o.billingAddress,o.billingCity,o.billingDistrict,o.billingWards,o.createDate,o.incurredCost,o.paid,pr.providerName,o.orderCode,o.note,o.adminNote) FROM OrderEntity o JOIN o.detailts d LEFT JOIN o.providerOrder pr  WHERE (o.billingFullName LIKE :keyword OR o.billingEmail LIKE :keyword OR o.billingPhoneNumber LIKE :keyword) AND o.trash=:trash AND o.status=:status AND pr.id=:host GROUP BY o")
    Page<OrderAdminDTO> findAllForOrderAdminDTOByStatus(@Param("keyword")String keyword,
                                                        @Param("trash") Boolean trash,
                                                        @Param("status") String status,
                                                        @Param("host") Integer host,
                                                        Pageable pageable);

    @Query("SELECT new com.nomi.caysenda.dto.OrderDTO(o.id,o.billingFullName,o.status,o.ship,o.orderAmount) FROM OrderEntity o LEFT JOIN o.userOrder u WHERE u.id=?1")
    Page<OrderDTO> findAllForOrderDTOByUserId(Integer userId,Pageable pageable);
    Long countByStatusNotAndTrash(String status,Boolean trash);
    Long countByStatusAndTrash(String status,Boolean trash);

    @Query("SELECT new com.nomi.caysenda.dto.OrderWithTrackingDTO(o.id,o.billingFullName,o.billingPhoneNumber,o.productAmount,o.orderAmount,o.billingEmail,o.billingAddress,o.billingCity,o.billingDistrict,o.billingWards,o.createDate,t.name,t.viStatusCode,t.id,t.packageNumber,t.packageReceived,o.cod,o.paid) FROM OrderEntity o LEFT JOIN o.tracking t WHERE t.ladingCode=:laddingCode ")
    List<OrderWithTrackingDTO> findALlByLaddingCode(@Param("laddingCode") String laddingCode);
    @Query("SELECT t FROM OrderEntity o LEFT JOIN o.tracking t WHERE t.ladingCode=:laddingCode AND t.id NOT in :ids")
    List<TrackingOrderEntity> findALlByLaddingCode(@Param("laddingCode") String laddingCode,
                                                   @Param("ids") List<Integer> ids);
    @Query("SELECT o FROM OrderEntity o  WHERE o.id IN :ids")
    List<OrderEntity> findAllByIdsWithSort(@Param("ids") List<Integer> ids, Sort sort);
}
