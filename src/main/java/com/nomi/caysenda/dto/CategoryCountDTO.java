package com.nomi.caysenda.dto;

public class CategoryCountDTO extends CategoryDTO{
    Long count;

    public CategoryCountDTO(Integer id, String name, String slug, String thumbnail, String banner, Integer parent, Long count) {
        super(id, name, slug, thumbnail, banner, parent);
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
