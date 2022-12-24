package com.nomi.caysenda.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;


@RedisHash(value = "cartvatiant",timeToLive = 86400L)
@Data
@AllArgsConstructor
public class RedisCartVariant {
    @Id
    Long id;
    @Indexed
    Integer variantId;
    String name;
    String thumbnail;
    String sku;
    Long priceDefault;
    Integer quantity;
    Long vip1;
    Long vip2;
    Long vip3;
    Long vip4;
    @Indexed
    Boolean active;
    @Indexed
    Long productCartId;
    Float weight;
    String nameZh;
    Long cost;
    String groupName;
    String groupZhName;
    String groupSku;

    public RedisCartVariant() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupZhName() {
        return groupZhName;
    }

    public void setGroupZhName(String groupZhName) {
        this.groupZhName = groupZhName;
    }

    public String getGroupSku() {
        return groupSku;
    }

    public void setGroupSku(String groupSku) {
        this.groupSku = groupSku;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(Long priceDefault) {
        this.priceDefault = priceDefault;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getProductCartId() {
        return productCartId;
    }

    public void setProductCartId(Long productCartId) {
        this.productCartId = productCartId;
    }
}
