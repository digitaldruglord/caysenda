
package com.nomi.caysenda.model;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.SimpleDateFormat;
import java.util.Date;

@Root(name = "sitemap")
public class Sitemap {
    @Element
    String loc;
    @Element
    String lastmod;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public Sitemap() {
    }

    public Sitemap(String loc, Date lastmod) {
        this.loc = loc;

        this.lastmod = simpleDateFormat.format(lastmod);
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
        this.lastmod = simpleDateFormat.format(lastmod)+"+00:00";
    }
}
