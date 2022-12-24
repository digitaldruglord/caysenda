package com.nomi.caysenda.lazada.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lazadatoken")
@Getter
@Setter
public class LazadaTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String token;
    Integer hostId;

}
