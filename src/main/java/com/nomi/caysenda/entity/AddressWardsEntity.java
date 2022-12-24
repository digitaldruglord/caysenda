package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "addressWards")
public class AddressWardsEntity {
    @Id String id;
    String name;
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "dictricId",referencedColumnName = "id")
    AddressDictrictEntity dictrictEntity;

    public AddressDictrictEntity getDictrictEntity() {
        return dictrictEntity;
    }

    public void setDictrictEntity(AddressDictrictEntity dictrictEntity) {
        this.dictrictEntity = dictrictEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
