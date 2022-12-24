package com.nomi.caysenda.api.admin.model.order.request;

public class AdminOrderDetailt {
    Integer id;
    Integer quantity;
    Integer owe;
    Long price;
    Double priceCN;
    Long cost;
    String name;
    String variantName;
    String productThumbnail;
    String variantThumbnail;
    Integer productId;
    Integer variantId;
    Integer categoryId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOwe() {
        return owe;
    }

    public void setOwe(Integer owe) {
        this.owe = owe;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Double getPriceCN() {
        return priceCN;
    }

    public void setPriceCN(Double priceCN) {
        this.priceCN = priceCN;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getVariantThumbnail() {
        return variantThumbnail;
    }

    public void setVariantThumbnail(String variantThumbnail) {
        this.variantThumbnail = variantThumbnail;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }
}
