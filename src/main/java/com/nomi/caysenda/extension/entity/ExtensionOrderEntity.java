package com.nomi.caysenda.extension.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "extensionOrder")
@Getter
@Setter
public class ExtensionOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer orderId;
    String status;
    String fullName;
    String phoneNumber;
    Boolean sub;
    @OneToMany(mappedBy = "orderEntity",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    List<ExtensionOrderDetailtEntity> detailts;


}
