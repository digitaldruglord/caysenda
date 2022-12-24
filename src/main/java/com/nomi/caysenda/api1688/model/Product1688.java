package com.nomi.caysenda.api1688.model;

public class Product1688 {
    String pageType;
    String offerid;
    Variant1688 variant;

    public Variant1688 getVariant() {
        return variant;
    }

    public void setVariant(Variant1688 variant) {
        this.variant = variant;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }
}
