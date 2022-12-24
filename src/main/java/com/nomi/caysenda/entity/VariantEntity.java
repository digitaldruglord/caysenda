package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "variants",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"skuZh"}),
        @UniqueConstraint(columnNames = {"skuVi"})
})
public class VariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition="text")
    String nameZh;
    String skuZh;
    @Column(columnDefinition="text")
    String nameVi;
    String skuVi;
    @Column(columnDefinition="text")
    String thumbnail;
    Float weight;
    Float width;
    Float height;
    Float length;
    Integer stock;
    Long price;
    Long vip1;
    Long vip2;
    Long vip3;
    Long vip4;
    Long cost;
    Double priceZh;
    String parent;
    String dimension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid",referencedColumnName = "id")
    @JsonIgnore
    ProductEntity productEntity;
    @OneToMany(mappedBy = "cartVariantEntity",cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<CartVariantEntity> cartVariants;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public List<CartVariantEntity> getCartVariants() {
        return cartVariants;
    }

    public void setCartVariants(List<CartVariantEntity> cartVariants) {
        this.cartVariants = cartVariants;
    }

    public Double getPriceZh() {
        return priceZh;
    }

    public void setPriceZh(Double priceZh) {
        this.priceZh = priceZh;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getSkuZh() {
        return skuZh;
    }

    public void setSkuZh(String skuZh) {
        this.skuZh = skuZh;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getSkuVi() {
        return skuVi;
    }

    public void setSkuVi(String skuVi) {
        this.skuVi = skuVi;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
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

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getVip1() {
        return vip1;
    }

    public void setVip1(Long vip1) {
        this.vip1 = vip1;
    }

    public Long getVip2() {
        return vip2;
    }

    public void setVip2(Long vip2) {
        this.vip2 = vip2;
    }

    public Long getVip3() {
        return vip3;
    }

    public void setVip3(Long vip3) {
        this.vip3 = vip3;
    }

    public Long getVip4() {
        return vip4;
    }

    public void setVip4(Long vip4) {
        this.vip4 = vip4;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
}
