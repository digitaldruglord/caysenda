package com.nomi.caysenda.lazada.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lazadavariants")
@Getter
@Setter
public class LazadaVariantEntity {
    @Id
    String sku_id;
    String shop_sku;
    @Column(unique = true)
    String seller_sku;
    @ManyToOne
    @JoinColumn(name = "item_id",referencedColumnName = "item_id")
    LazadaProductEntity lazadaProductEntity;
    Integer categoryId;

}
