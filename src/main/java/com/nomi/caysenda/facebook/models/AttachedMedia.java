package com.nomi.caysenda.facebook.models;

public class AttachedMedia {
    String media_fbid;

    public AttachedMedia() {
    }

    public AttachedMedia(String media_fbid) {
        this.media_fbid = media_fbid;
    }

    public String getMedia_fbid() {
        return media_fbid;
    }

    public void setMedia_fbid(String media_fbid) {
        this.media_fbid = media_fbid;
    }
}
