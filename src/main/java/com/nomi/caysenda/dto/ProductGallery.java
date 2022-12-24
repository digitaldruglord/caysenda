package com.nomi.caysenda.dto;

import java.util.List;

public class ProductGallery extends ProductDTO {
    List<String> gallery;

    public ProductGallery(Integer id, String name, String slug, String sku, Long minPrice, Long maxPrice, String thumbnail, String categoryName, String categorySlug, List<String> gallery) {
        super(id, name, slug, sku, minPrice, maxPrice, thumbnail, categoryName, categorySlug);
        this.gallery = gallery;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}
