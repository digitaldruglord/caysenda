package com.nomi.caysenda.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Root(name = "url")
public class SitemapUrl {

    @Element
    String loc;
    @Element
    String lastmod;
    @Element
    Float priority;

    @ElementList(inline = true,required = false)
    List<SitemapImage> images;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public SitemapUrl() {
    }

    public SitemapUrl(String loc, Date lastmod) {
        this.loc = loc;
        this.lastmod = format.format(lastmod);
    }

    public SitemapUrl(String loc, Date lastmod, Float priority) {
        this.loc = loc;
        this.lastmod = format.format(lastmod);
        this.priority = priority;
    }

    public List<SitemapImage> getImages() {
        return images;
    }

    public void setImages(List<SitemapImage> images) {
        this.images = images;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLastmod() {
        return lastmod;
    }

    public void setLastmod(Date lastmod) {
        this.lastmod = format.format(lastmod)+"+00:00";
    }



    public Float getPriority() {
        return priority;
    }

    public void setPriority(Float priority) {
        this.priority = priority;
    }
}
