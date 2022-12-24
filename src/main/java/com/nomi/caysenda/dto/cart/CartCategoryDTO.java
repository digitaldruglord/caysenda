package com.nomi.caysenda.dto.cart;

import java.util.List;

public class CartCategoryDTO {
    Integer id;
    String name;
    String slug;
    Long condition;
    List<CartProductDTO> products;
    Boolean active;
    Long amount;
    Long amountActive;


    public CartCategoryDTO() {
    }

    public CartCategoryDTO(Integer id) {
        this.id = id;
    }

    public CartCategoryDTO(Integer id, String name, String slug, Long condition, List<CartProductDTO> products) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.condition = condition;
        this.products = products;
    }

    public Long getAmount() {
        return amount;
    }

    public Long getAmountActive() {
        return amountActive;
    }

    public void setAmountActive(Long amountActive) {
        this.amountActive = amountActive;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getCondition() {
        return condition;
    }

    public void setCondition(Long condition) {
        this.condition = condition;
    }

    public List<CartProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductDTO> products) {
        this.products = products;
    }
}
