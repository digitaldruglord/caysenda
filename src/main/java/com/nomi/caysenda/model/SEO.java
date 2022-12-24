package com.nomi.caysenda.model;

import java.util.List;

public class SEO {
    String canonical;
    String description;
    String robots;
    String keywords;
    String title;
    String image;
    List<String> dns_prefetch;
    String schema;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCanonical() {
        return canonical;
    }

    public void setCanonical(String canonical) {
        this.canonical = canonical;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRobots() {
        return robots;
    }

    public void setRobots(String robots) {
        this.robots = robots;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getDns_prefetch() {
        return dns_prefetch;
    }

    public void setDns_prefetch(List<String> dns_prefetch) {
        this.dns_prefetch = dns_prefetch;
    }
}
