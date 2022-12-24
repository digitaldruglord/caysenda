package com.nomi.caysenda.model;

import org.simpleframework.xml.*;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Root(name = "urlset")
@NamespaceList({
        @Namespace(reference="http://www.sitemaps.org/schemas/sitemap/0.9"),
        @Namespace(reference="http://www.w3.org/2001/XMLSchema-instance", prefix="xsi"),
        @Namespace(reference="http://www.google.com/schemas/sitemap-image/1.1", prefix="image")
})
public class SitemapUrlSet {
    @Attribute(name = "schemaLocation")
    @Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi")
    private String mSchemaLocation;

    @ElementList(inline = true)
    List<SitemapUrl> urls;

    public SitemapUrlSet() {
        this.mSchemaLocation = "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd http://www.google.com/schemas/sitemap-image/1.1 http://www.google.com/schemas/sitemap-image/1.1/sitemap-image.xsd";
    }

    public SitemapUrlSet(List<SitemapUrl> urls) {
        this.urls = urls;
    }

    public List<SitemapUrl> getUrls() {
        return urls;
    }
    @XmlElement(name = "url")
    public void setUrls(List<SitemapUrl> urls) {
        this.urls = urls;
    }

    public String getmSchemaLocation() {
        return mSchemaLocation;
    }

    public void setmSchemaLocation(String mSchemaLocation) {
        this.mSchemaLocation = mSchemaLocation;
    }
}
