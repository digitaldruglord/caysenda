package com.nomi.caysenda.dto;

import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.model.responses.OrderSummary;

import java.util.List;

public class OrderDTO {
    Integer id;
    String name;
    String status;
    Long fee;
    Long amount;
    List<OrderSummary> ghnOrderEntities;
    public OrderDTO() {
    }

    public OrderDTO(Integer id, String name, String status, Long fee, Long amount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.fee = fee;
        this.amount = amount;
    }

    public List<OrderSummary> getGhnOrderEntities() {
        return ghnOrderEntities;
    }

    public void setGhnOrderEntities(List<OrderSummary> ghnOrderEntities) {
        this.ghnOrderEntities = ghnOrderEntities;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
