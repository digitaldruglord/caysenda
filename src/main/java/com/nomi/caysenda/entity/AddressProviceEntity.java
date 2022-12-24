package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "addressProvince")
public class AddressProviceEntity {
    @Id String id;
    String name;

    @OneToMany(mappedBy = "proviceEntity")
    @JsonIgnore
    List<AddressDictrictEntity> dictricts;

    public List<AddressDictrictEntity> getDictricts() {
        return dictricts;
    }

    public void setDictricts(List<AddressDictrictEntity> dictricts) {
        this.dictricts = dictricts;
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
