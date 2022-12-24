package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "addressDictrict")
public class AddressDictrictEntity {
    @Id String id;
    String name;

    @OneToMany(mappedBy = "dictrictEntity")
    @JsonIgnore
    List<AddressWardsEntity> wards;

    @ManyToOne()
    @JoinColumn(name = "provinceId",referencedColumnName = "id")
    @JsonIgnore
    AddressProviceEntity proviceEntity;

    public AddressProviceEntity getProviceEntity() {
        return proviceEntity;
    }

    public void setProviceEntity(AddressProviceEntity proviceEntity) {
        this.proviceEntity = proviceEntity;
    }

    public List<AddressWardsEntity> getWards() {
        return wards;
    }

    public void setWards(List<AddressWardsEntity> wards) {
        this.wards = wards;
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
