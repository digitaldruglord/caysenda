package com.nomi.caysenda.dto.cart;

public class CartStatictis {
    Integer id;
    String name;
    Long cost;
    Long productDefaultAmount;
    Long productVip1Amount;
    Long productVip2Amount;
    Long productVip3Amount;
    Long productVip4Amount;

    public CartStatictis() {
    }

    public CartStatictis(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getProductDefaultAmount() {
        return productDefaultAmount;
    }

    public void setProductDefaultAmount(Long productDefaultAmount) {
        this.productDefaultAmount = productDefaultAmount;
    }

    public Long getProductVip1Amount() {
        return productVip1Amount;
    }

    public void setProductVip1Amount(Long productVip1Amount) {
        this.productVip1Amount = productVip1Amount;
    }

    public Long getProductVip2Amount() {
        return productVip2Amount;
    }

    public void setProductVip2Amount(Long productVip2Amount) {
        this.productVip2Amount = productVip2Amount;
    }

    public Long getProductVip3Amount() {
        return productVip3Amount;
    }

    public void setProductVip3Amount(Long productVip3Amount) {
        this.productVip3Amount = productVip3Amount;
    }

    public Long getProductVip4Amount() {
        return productVip4Amount;
    }

    public void setProductVip4Amount(Long productVip4Amount) {
        this.productVip4Amount = productVip4Amount;
    }
}
