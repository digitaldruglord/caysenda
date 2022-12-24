package com.nomi.caysenda.dto;

public class ReportOrderDTO {
    Long netRevenue;
    Long grossRevenue;
    Long cost;
    Long ship;
    Long incurredCost;
    String province;
    Long profit;
    Float profitPercent;

    public ReportOrderDTO() {
    }

    public ReportOrderDTO(Long netRevenue, Long grossRevenue, Long cost, Long ship, Long incurredCost, String province) {
        this.netRevenue = netRevenue;
        this.grossRevenue = grossRevenue;
        this.cost = cost;
        this.ship = ship;
        this.incurredCost = incurredCost;
        this.province = province;
    }

    public Long getProfit() {
        return profit;
    }

    public void setProfit(Long profit) {
        this.profit = profit;
    }

    public Float getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(Float profitPercent) {
        this.profitPercent = profitPercent;
    }

    public Long getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(Long netRevenue) {
        this.netRevenue = netRevenue;
    }

    public Long getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(Long grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getShip() {
        return ship;
    }

    public void setShip(Long ship) {
        this.ship = ship;
    }

    public Long getIncurredCost() {
        return incurredCost;
    }

    public void setIncurredCost(Long incurredCost) {
        this.incurredCost = incurredCost;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
