package com.nomi.caysenda.extension.model;

import java.util.List;

public class ExtensionProduct {
    String nameZh;
    String standardName;
    Double price1;
    Double price2;
    Double price3;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    String link;
    List<String> gallery;
    String sku;
    Integer quantity;
    String unit;
    String video;
    List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public Double getPrice3() {
        return price3;
    }

    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    public Integer getCondition1() {
        return condition1;
    }

    public void setCondition1(Integer condition1) {
        this.condition1 = condition1;
    }

    public Integer getCondition2() {
        return condition2;
    }

    public void setCondition2(Integer condition2) {
        this.condition2 = condition2;
    }

    public Integer getCondition3() {
        return condition3;
    }

    public void setCondition3(Integer condition3) {
        this.condition3 = condition3;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}
