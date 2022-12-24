package com.nomi.caysenda.dto.cart;

public class CartVariantDTO {
    Long id;
    String name;
    String thumbnail;
    String sku;
    Long price;
    Integer quantity;
    Long priceDefault;
    Long vip1;
    Long vip2;
    Long vip3;
    Long vip4;
    Long cost;
    Boolean active;
    Integer variantId;
    Float weight;
    String nameZh;
    String groupName;
    String groupZhName;
    String groupSku;



    public CartVariantDTO() {
    }

    public CartVariantDTO(Long id, String name, String thumbnail, String sku, Long price, Integer quantity, Long priceDefault, Long vip1, Long vip2, Long vip3, Long vip4, Long cost, Boolean active, Integer variantId, Float weight, String nameZh, String groupName, String groupZhName, String groupSku) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.priceDefault = priceDefault;
        this.vip1 = vip1;
        this.vip2 = vip2;
        this.vip3 = vip3;
        this.vip4 = vip4;
        this.cost = cost;
        this.active = active;
        this.variantId = variantId;
        this.weight = weight;
        this.nameZh = nameZh;
        this.groupName = groupName;
        this.groupZhName = groupZhName;
        this.groupSku = groupSku;
    }

    public CartVariantDTO(Long id, String name, String thumbnail, String sku, Long price, Integer quantity, Long priceDefault, Long vip1, Long vip2, Long vip3, Long vip4, Long cost, Boolean active, Integer variantId, Float weight, String nameZh) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.priceDefault = priceDefault;
        this.vip1 = vip1;
        this.vip2 = vip2;
        this.vip3 = vip3;
        this.vip4 = vip4;
        this.cost = cost;
        this.active = active;
        this.variantId = variantId;
        this.weight = weight;
        this.nameZh = nameZh;
    }

    public CartVariantDTO(Long id, String name, String thumbnail, String sku, Long price, Integer quantity, Long vip1, Long vip2, Long vip3, Long vip4, Long cost, Boolean active, Integer variantId, Float weight) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.vip1 = vip1;
        this.vip2 = vip2;
        this.vip3 = vip3;
        this.vip4 = vip4;
        this.cost = cost;
        this.active = active;
        this.variantId = variantId;
        this.weight = weight;
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

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public Long getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(Long priceDefault) {
        this.priceDefault = priceDefault;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
