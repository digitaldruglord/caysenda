package com.nomi.caysenda.extension.model.request;

public class ExtensionUpdateVariantRequest {
    Integer id;
    String standardName;
    Double price;
    Long priceDefault;
    Long price1;
    Long price2;
    Long price3;
    Long price4;
    Float length;
    Float width;
    Float height;
    Float weight;
    Integer stock;
    String dimension;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(Long priceDefault) {
        this.priceDefault = priceDefault;
    }

    public Long getPrice1() {
        return price1;
    }

    public void setPrice1(Long price1) {
        this.price1 = price1;
    }

    public Long getPrice2() {
        return price2;
    }

    public void setPrice2(Long price2) {
        this.price2 = price2;
    }

    public Long getPrice3() {
        return price3;
    }

    public void setPrice3(Long price3) {
        this.price3 = price3;
    }

    public Long getPrice4() {
        return price4;
    }

    public void setPrice4(Long price4) {
        this.price4 = price4;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
