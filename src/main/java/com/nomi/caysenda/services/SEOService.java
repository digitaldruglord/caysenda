package com.nomi.caysenda.services;

import com.nomi.caysenda.dto.ProductSEO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.PageEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.model.SEO;
import com.nomi.caysenda.options.model.WebsiteInfo;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface SEOService {
    SEO getSEO(String slug,WebsiteInfo websiteInfo);
    SEO getDefault(WebsiteInfo websiteInfo);
    SEO getSEOHome(WebsiteInfo websiteInfo);
    SEO getSEOProduct(String slug,String categorySlug,WebsiteInfo websiteInfo);
    SEO getSEOCategory(String slug,WebsiteInfo websiteInfo);
    SEO getSEOPage(String slug,WebsiteInfo websiteInfo);
    String getHomeSchema(WebsiteInfo websiteInfo);
    String getDefaultSchema(WebsiteInfo websiteInfo);
    String getCategorySchema(String slug, CategoryEntity categoryEntity, WebsiteInfo websiteInfo);
    String getPageSchema(String slug, PageEntity pageEntity,WebsiteInfo websiteInfo);
    String getProductSchema(String url, ProductSEO productEntity, WebsiteInfo websiteInfo);
    List<String> getDNS_PREFETCH();
    String descriptionDefault(WebsiteInfo websiteInfo);
    void createSitemap() throws FileNotFoundException;
}
