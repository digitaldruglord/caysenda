
package com.nomi.caysenda.model;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "sitemapindex")
@Namespace(reference = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class SitemapIndex {
    @ElementList(inline=true)
    List<Sitemap> sitemap;


    public SitemapIndex() {
    }

    public SitemapIndex(List<Sitemap> sitemap) {
        this.sitemap = sitemap;
    }


    public List<Sitemap> getSitemap() {
        return sitemap;
    }

    public void setSitemap(List<Sitemap> sitemap) {
        this.sitemap = sitemap;
    }
}
