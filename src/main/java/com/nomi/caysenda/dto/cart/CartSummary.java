package com.nomi.caysenda.dto.cart;

import com.nomi.caysenda.entity.AddressEntity;

import java.util.Date;
import java.util.List;

public class CartSummary {
    Integer id;
    String name;
    Long cost;
    Long amount;
    Long profit;
    String note;
    Integer userId;
    Date createDate;
    Date modifiedDate;
    String phone;
    List<String> address;

    public CartSummary(Integer id, String name, String note, Integer userId) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.userId = userId;
    }

    public CartSummary(Integer id, String name, Long cost, String note, Integer userId, Date createDate, Date modifiedDate, String phone) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.note = note;
        this.userId = userId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.phone = phone;
    }

    public CartSummary(Integer id, String name, Long cost, String note, Integer userId, Date createDate, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.note = note;
        this.userId = userId;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getProfit() {
        return profit;
    }

    public void setProfit(Long profit) {
        this.profit = profit;
    }
}
