package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trackingorder")
@Getter
@Setter
public class TrackingOrderEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String orderCode;
    Integer quantity;
    String status;
    String statusZh;
    String note;
    Integer packageNumber;
    Date receiptDate;
    String carrier;
    String ladingCode;
    String productAmount;
    Date dateOrder;
    String viStatusCode;
    Integer packageReceived;
    Boolean received;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId",referencedColumnName = "id")
    @JsonIgnore
    OrderEntity orderTracking;

}
