package com.nomi.caysenda.dto;

public class AddressDTO {
    Integer id;
    String phoneNumber;
    String fullname;
    String email;
    String address;
    String province;
    String dictrict;
    String ward;
    Boolean active;
    String fullAddress;


    public AddressDTO() {
    }

    public AddressDTO(Integer id, String phoneNumber, String fullname, String email, String address, String province, String dictrict, String ward, Boolean active) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.province = province;
        this.dictrict = dictrict;
        this.ward = ward;
        this.active = active;
    }

    public AddressDTO(Integer id, String phoneNumber, String fullname, String email, String address, String province, String dictrict, String ward) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.province = province;
        this.dictrict = dictrict;
        this.ward = ward;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
