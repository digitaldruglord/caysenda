package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String phoneNumber;
    String fullname;
    String email;
    String address;
    String province;
    String dictrict;
    String ward;
    Boolean active;
    @ManyToOne()
    @JoinColumn(name = "userId",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    UserEntity userAddress;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserEntity getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserEntity userAddress) {
        this.userAddress = userAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDictrict() {
        return dictrict;
    }

    public void setDictrict(String dictrict) {
        this.dictrict = dictrict;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
