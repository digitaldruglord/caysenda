package com.nomi.caysenda.lazada.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lazadaproducts")
@Getter
@Setter
public class LazadaProductEntity {
    @Id
    String item_id;
    String productSku;
    @OneToMany(mappedBy = "lazadaProductEntity",cascade = CascadeType.ALL)
    List<LazadaVariantEntity> variants;
    Integer categoryId;


}
