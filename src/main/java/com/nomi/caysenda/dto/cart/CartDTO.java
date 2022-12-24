package com.nomi.caysenda.dto.cart;

import org.springframework.data.redis.core.RedisHash;

import java.util.List;

public class CartDTO {
    List<CartCategoryDTO> categories;


    public CartDTO() {
    }

    public CartDTO(List<CartCategoryDTO> categories) {
        this.categories = categories;
    }

    public List<CartCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CartCategoryDTO> categories) {
        this.categories = categories;
    }
}
