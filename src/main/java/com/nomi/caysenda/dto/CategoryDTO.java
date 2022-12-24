package com.nomi.caysenda.dto;

public class CategoryDTO {
    private Integer id;
    private String name;
    private String slug;
    private String thumbnail;
    private String banner;
    private Integer parent;
    private Long count;
    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String name, String slug, String thumbnail, String banner, Integer parent) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.thumbnail = thumbnail;
        this.banner = banner;
        this.parent = parent;
    }

    public CategoryDTO(Integer id, String name, String slug, String thumbnail, String banner, Integer parent, Long count) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.thumbnail = thumbnail;
        this.banner = banner;
        this.parent = parent;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}
