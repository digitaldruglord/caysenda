package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orderDetailt")
@Getter
@Setter
public class OrderDetailtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer quantity;
    Integer owe;
    Long price;
    Double priceCN;
    Long cost;
    String name;
    String variantName;
    String productThumbnail;
    String variantThumbnail;
    Integer productId;
    Integer variantId;
    Float weight;
    String slug;
    String linkZh;
    String sku;
    String nameZh;
    String variantNameZh;
    String groupName;
    String groupZhName;
    String groupSku;
    Integer categoryId;
    String unit;

    @ManyToOne()
    @JoinColumn(name = "orderId",referencedColumnName = "id")
    @JsonIgnore
    OrderEntity orderEntity;
}
