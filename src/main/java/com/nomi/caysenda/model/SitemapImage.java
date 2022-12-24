package com.nomi.caysenda.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "image")
@Namespace(prefix="image", reference="http://www.google.com/schemas/sitemap-image/1.1")
public class SitemapImage {
    @Element
    @Namespace(prefix="image", reference="http://www.google.com/schemas/sitemap-image/1.1")
    String loc;
    @Element(data = true)
    @Namespace(prefix="image", reference="http://www.google.com/schemas/sitemap-image/1.1")
    String title;
    @Namespace(prefix="image", reference="http://www.google.com/schemas/sitemap-image/1.1")
    @Element(data = true)
    String caption;

    public SitemapImage() {
    }

    public SitemapImage(String loc, String title, String caption) {
        this.loc = loc;
        this.title = title;
        this.caption = caption;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
