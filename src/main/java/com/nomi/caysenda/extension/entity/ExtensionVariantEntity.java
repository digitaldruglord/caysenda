package com.nomi.caysenda.extension.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "extensionVariants")
public class ExtensionVariantEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition="text")
    String nameZh;
    @Column(columnDefinition = "text")
    String tempName;
    @Column(columnDefinition="text")
    String standardName;
    String img;
    Double price;
    Integer stock;
    String unit;
    String parent;
    String parentTemp;
    Long priceDefault;
    Long price1;
    Long price2;
    Long price3;
    Long price4;
    @Column(precision = 0)
    Float length;
    @Column(precision = 0)
    Float width;
    @Column(precision = 0)
    Float height;
    @Column(precision = 0)
    Float weight;
    String sku;
    String dimension;



    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "productId",referencedColumnName = "id")
    ExtensionProductEntity product;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getParentTemp() {
        return parentTemp;
    }

    public void setParentTemp(String parentTemp) {
        this.parentTemp = parentTemp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public ExtensionProductEntity getProduct() {
        return product;
    }

    public void setProduct(ExtensionProductEntity product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
