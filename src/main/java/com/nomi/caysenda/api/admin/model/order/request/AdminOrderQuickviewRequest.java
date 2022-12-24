package com.nomi.caysenda.api.admin.model.order.request;

public class AdminOrderQuickviewRequest {
    Integer id;
    Long ship;
    Long cost;
    Long incurredCost;
    Long paid;
    String note;
    String adminNote;

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
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

    public Long getShip() {
        return ship;
    }

    public void setShip(Long ship) {
        this.ship = ship;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getIncurredCost() {
        return incurredCost;
    }

    public void setIncurredCost(Long incurredCost) {
        this.incurredCost = incurredCost;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }
}
