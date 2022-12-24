package com.nomi.caysenda.facebook.models;

public class UploadImageRequest {
    String url;
    Boolean published;

    public UploadImageRequest() {
    }

    public UploadImageRequest(String url, Boolean published) {
        this.url = url;
        this.published = published;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
