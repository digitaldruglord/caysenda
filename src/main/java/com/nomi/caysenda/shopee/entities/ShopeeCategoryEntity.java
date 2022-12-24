package com.nomi.caysenda.shopee.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shopeecategory")
@Getter
@Setter
public class ShopeeCategoryEntity {
    @Id
    Integer id;
    @Column(columnDefinition = "text")
    String name;

}
