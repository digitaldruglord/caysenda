package com.nomi.caysenda.facebook.models;

import java.util.List;

public class CreatePostRequest {
    String message;
    List<AttachedMedia> attached_media;
    Boolean published;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AttachedMedia> getAttached_media() {
        return attached_media;
    }

    public void setAttached_media(List<AttachedMedia> attached_media) {
        this.attached_media = attached_media;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
