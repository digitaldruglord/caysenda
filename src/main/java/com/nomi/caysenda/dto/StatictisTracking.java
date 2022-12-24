package com.nomi.caysenda.dto;

public class StatictisTracking {
    Double totalCode;
    Integer totalPackage;

    public StatictisTracking(Double totalCode, Integer totalPackage) {
        this.totalCode = totalCode;
        this.totalPackage = totalPackage;
    }

    public Double getTotalCode() {
        return totalCode;
    }

    public void setTotalCode(Double totalCode) {
        this.totalCode = totalCode;
    }

    public Integer getTotalPackage() {
        return totalPackage;
    }

    public void setTotalPackage(Integer totalPackage) {
        this.totalPackage = totalPackage;
    }
}
