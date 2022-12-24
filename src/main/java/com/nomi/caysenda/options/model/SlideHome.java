package com.nomi.caysenda.options.model;

public class SlideHome {
    Integer id;
    String title;
    String description;
    Long price;
    String href;
    String thumbnail;

    public SlideHome() {
    }

    public SlideHome(String title, String description, Long price, String href, String thumbnail) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.href = href;
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
