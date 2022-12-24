package com.nomi.caysenda.options.model;

public class BannerTOP {
    Integer id;
    String thumbnail;
    String content;
    String href;

    public BannerTOP() {
    }

    public BannerTOP(String thumbnail, String content, String href) {
        this.thumbnail = thumbnail;
        this.content = content;
        this.href = href;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
