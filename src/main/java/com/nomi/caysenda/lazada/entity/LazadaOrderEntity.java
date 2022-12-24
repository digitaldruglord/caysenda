package com.nomi.caysenda.lazada.entity;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lazadaorders")
@Getter
@Setter
public class LazadaOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    Integer orderId;
    @Column(unique = true)
    String lazadaId;
}
