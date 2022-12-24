package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.dto.ProductSEO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.PageEntity;
import com.nomi.caysenda.model.*;
import com.nomi.caysenda.options.model.WebsiteInfo;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.PageService;
import com.nomi.caysenda.services.SEOService;
import com.nomi.caysenda.utils.SpringUtils;
import org.apache.commons.io.FilenameUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class SEOServiceImpl implements SEOService {
    @Autowired
    Environment env;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PageService pageService;
    @Override
    public SEO getSEO(String slug, WebsiteInfo websiteInfo) {
        String[] strings = slug.split("/");
        switch (strings.length){
            case 0: return getSEOHome(websiteInfo);
            case 2: return getSEOCategory(strings[1],websiteInfo);
            case 3: return getSEOProduct(strings[2],strings[1],websiteInfo);
            default: return getDefault(websiteInfo);
        }
    }

    @Override
    public SEO getDefault(WebsiteInfo websiteInfo) {
        SEO seo = new SEO();
        seo.setCanonical(env.getProperty("spring.domain"));
        seo.setDescription(websiteInfo.getDescription()!=null?websiteInfo.getDescription():descriptionDefault(websiteInfo));
        seo.setTitle(websiteInfo.getTitle());
        seo.setImage(websiteInfo.getBannerFooter());
        seo.setDns_prefetch(getDNS_PREFETCH());
        seo.setKeywords(websiteInfo.getDescription());
        seo.setSchema(getDefaultSchema(websiteInfo));
        return seo;
    }

    @Override
    public SEO getSEOHome(WebsiteInfo websiteInfo) {
        SEO seo = new SEO();
        seo.setCanonical(env.getProperty("spring.domain"));
        seo.setDescription(websiteInfo.getDescription()!=null?websiteInfo.getDescription():descriptionDefault(websiteInfo));
        seo.setTitle(websiteInfo.getTitle());
        seo.setImage(websiteInfo.getBannerFooter());
        seo.setDns_prefetch(getDNS_PREFETCH());
        seo.setSchema(getHomeSchema(websiteInfo));
        seo.setKeywords(websiteInfo.getDescription());
        return seo;
    }

    @Override
    public SEO getSEOProduct(String slug,String categorySlug, WebsiteInfo websiteInfo) {
        ProductSEO productEntity = productRepository.findBySlugForProductSEO(slug);
        SEO seo = new SEO();
        if (productEntity!=null && productEntity.getId()!=null){
            seo.setCanonical(env.getProperty("spring.domain")+"/"+categorySlug+"/"+slug);
            seo.setDescription(productEntity.getDescription()!=null?productEntity.getDescription():descriptionDefault(websiteInfo));
            seo.setTitle(productEntity.getName());
            seo.setImage(productEntity.getThumbnail());
            seo.setSchema(getProductSchema(categorySlug+"/"+slug,productEntity,websiteInfo));
            seo.setKeywords(productEntity.getKeywords());
        }else {
            return getDefault(websiteInfo);
        }
        seo.setDns_prefetch(getDNS_PREFETCH());
        return seo;
    }

    @Override
    public SEO getSEOCategory(String slug, WebsiteInfo websiteInfo) {
        CategoryEntity categoryEntity = categoryService.findBySlug(slug).orElse(null);
        if (categoryEntity==null) return getSEOPage(slug,websiteInfo);
        SEO seo = new SEO();
        seo.setCanonical(env.getProperty("spring.domain")+"/"+slug);
        seo.setDescription(categoryEntity.getDescription()!=null?categoryEntity.getDescription():descriptionDefault(websiteInfo));
        seo.setTitle(categoryEntity.getName());
        seo.setImage(categoryEntity.getThumbnail());
        seo.setDns_prefetch(getDNS_PREFETCH());
        seo.setSchema(getCategorySchema(slug,categoryEntity,websiteInfo));
        return seo;
    }

    @Override
    public SEO getSEOPage(String slug, WebsiteInfo websiteInfo) {
        PageEntity pageEntity = pageService.findBySlug(slug);
        SEO seo = new SEO();
        if (pageEntity!=null){
            seo.setCanonical(env.getProperty("spring.domain")+"/"+slug);
            seo.setDescription(pageEntity.getDescription()!=null?pageEntity.getDescription():descriptionDefault(websiteInfo));
            seo.setTitle(pageEntity.getName());
            seo.setImage(pageEntity.getImage());
            seo.setSchema(getPageSchema(slug,pageEntity,websiteInfo));
        }
        seo.setDns_prefetch(getDNS_PREFETCH());
        return seo;
    }


    @Override
    public String getHomeSchema(WebsiteInfo websiteInfo) {
        Map map = new HashMap();
        /** static data*/
        String domain = env.getProperty("spring.domain");
        /** graph*/
        List<Map> graphList = new ArrayList<>();
        // website
            graphList.add(Map.of(
                    "@type","WebSite",
                    "@id",env.getProperty("spring.domain")+"/#website",
                    "url",domain,
                    "name",websiteInfo.getTitle(),
                    "potentialAction",Map.of(
                            "@type","SearchAction",
                            "target",domain+"/san-pham?keyword={search_term_string}",
                            "query-input","required name=search_term_string"
                    )
            ));
        // imageobject
        graphList.add(Map.of(
                "@type","ImageObject",
                "@id",domain+"/#primaryimage",
                "url",domain+websiteInfo.getBannerFooter(),
                "width",800,
                "height",800,
                "caption",800
        ));
        // webpage
        graphList.add(Map.of(
                "@type","WebPage",
                "@id",domain+"/#primaryimage",
                "url",domain,
                "inLanguage","vi-VN",
                "name",websiteInfo.getTitle(),
                "isPartOf",Map.of(
                        "@id",domain
                ),
                "primaryImageOfPage",Map.of(
                        "@id",domain
                ),
                "description",descriptionDefault(websiteInfo)
        ));
        /***/
        map.put("@context","https://schema.org");
        map.put("@graph",graphList);
        return SpringUtils.convertObjectToJson(map).replace("\n","").replace("\t","");
    }

    @Override
    public String getDefaultSchema(WebsiteInfo websiteInfo) {
        Map map = new HashMap();
        String domain = env.getProperty("spring.domain");
        map.put("@context","http://schema.org");
        map.put("@type","WebSite");
        map.put("name",websiteInfo.getTitle());
        map.put("alternateName",websiteInfo.getTitle());
        map.put("url",domain);
        map.put("potentialAction",Map.of(
                "@type","SearchAction",
                "target",domain+"/san-pham?keyword={search_term_string}",
                "query-input","required name=search_term_string"
        ));

        return SpringUtils.convertObjectToJson(map).replace("\n","").replace("\t","");
    }

    @Override
    public String getCategorySchema(String slug,CategoryEntity categoryEntity,WebsiteInfo websiteInfo) {
//        Map map = new HashMap();
//        /** static data*/
//        String domain = env.getProperty("spring.domain");
//        /** graph*/
//        List<Map> graphList = new ArrayList<>();
//        // website
//        graphList.add(Map.of(
//                "@type","WebSite",
//                "@id",env.getProperty("spring.domain")+"/#website",
//                "url",domain,
//                "name",websiteInfo.getTitle(),
//                "potentialAction",Map.of(
//                        "@type","SearchAction",
//                        "target",domain+"/san-pham?keyword={search_term_string}",
//                        "query-input","required name=search_term_string"
//                )
//        ));
//        // webpage
//        graphList.add(Map.of(
//                "@type","CollectionPage",
//                "@id",domain+"/"+slug+"/#webpage",
//                "url",domain+"/"+slug,
//                "inLanguage","vi-VN",
//                "name",categoryEntity.getName(),
//                "isPartOf",Map.of(
//                        "@id",domain+"/#website"
//                ),
//                "description",categoryEntity.getDescription()!=null?categoryEntity.getDescription():descriptionDefault(websiteInfo)
//        ));
//        /***/
//        map.put("@context","https://schema.org");
//        map.put("@graph",graphList);
        Map map = new HashMap();
        String domain = env.getProperty("spring.domain");
        map.put("@context","http://schema.org");
        map.put("@type","WebSite");
        map.put("name",websiteInfo.getTitle());
        map.put("alternateName",websiteInfo.getTitle());
        map.put("url",domain);
        map.put("potentialAction",Map.of(
                "@type","SearchAction",
                "target",domain+"/san-pham?keyword={search_term_string}",
                "query-input","required name=search_term_string"
        ));
        return SpringUtils.convertObjectToJson(map).replace("\n","").replace("\t","");
    }

    @Override
    public String getPageSchema(String slug, PageEntity pageEntity, WebsiteInfo websiteInfo) {
        Map map = new HashMap();
        if (pageEntity.getType().equals("page")){
            String domain = env.getProperty("spring.domain");
            map.put("@context","http://schema.org");
            map.put("@type","WebSite");
            map.put("name",websiteInfo.getTitle());
            map.put("alternateName",websiteInfo.getTitle());
            map.put("url",domain);
            map.put("potentialAction",Map.of(
                    "@type","SearchAction",
                    "target",domain+"/san-pham?keyword={search_term_string}",
                    "query-input","required name=search_term_string"
            ));
        }else {
            /** static data*/
            String domain = env.getProperty("spring.domain");
            /** graph*/
            List<Map> graphList = new ArrayList<>();
            // website
            graphList.add(Map.of(
                    "@type","WebSite",
                    "@id",env.getProperty("spring.domain")+"/#website",
                    "url",domain,
                    "name",websiteInfo.getTitle(),
                    "potentialAction",Map.of(
                            "@type","SearchAction",
                            "target",domain+"/san-pham?keyword={search_term_string}",
                            "query-input","required name=search_term_string"
                    )
            ));
            // imageobject
            graphList.add(Map.of(
                    "@type","ImageObject",
                    "@id",domain+"/"+slug+"/#primaryimage",
                    "url",domain+pageEntity.getImage(),
                    "width",800,
                    "height",800,
                    "caption", FilenameUtils.getBaseName(pageEntity.getImage())
            ));
            // webpage
            graphList.add(Map.of(
                    "@type","WebPage",
                    "@id",domain+"/"+slug+"/#webpage",
                    "url",domain+"/"+slug,
                    "inLanguage","vi-VN",
                    "name",pageEntity.getName(),
                    "isPartOf",Map.of(
                            "@id",domain+"/#website"
                    ),
                    "primaryImageOfPage",Map.of("@id",domain+slug+"/#primaryimage"),
                    "description",pageEntity.getDescription()!=null?pageEntity.getDescription():descriptionDefault(websiteInfo)
            ));
            /***/
            map.put("@context","https://schema.org");
            map.put("@graph",graphList);
        }

        return SpringUtils.convertObjectToJson(map).replace("\n","").replace("\t","");
    }

    @Override
    public String getProductSchema(String url,ProductSEO productEntity,WebsiteInfo websiteInfo) {
        Map map = new HashMap();
        /** static data*/
        String domain = env.getProperty("spring.domain");
        map.put("@context","https://schema.org");
        map.put("@type","Product");
        map.put("name",productEntity.getName());
        map.put("image",productEntity.getThumbnail());
        map.put("description",productEntity.getDescription());
        map.put("mpn",productEntity.getId());
        /** Review */
        Map review = Map.of(
                "@type","Review",
                "reviewRating",Map.of("@type","Rating","ratingValue",5),
                "reviewBody","Tu van giup!"+websiteInfo.getPhoneNumber(),
                "author",Map.of("@type","Person","name",websiteInfo.getTitle())
        );
        map.put("review",review);
        map.put("aggregateRating",Map.of("@type","AggregateRating","ratingValue",5,"reviewCount",100));
        map.put("offers",Map.of(
                "@type","Offer",
                "priceCurrency","VND",
                "price",productEntity.getPrice(),
                "priceValidUntil","2021-07-22",
                "itemCondition","http://schema.org/UsedCondition",
                "availability","http://schema.org/InStock",
                "seller",Map.of("@type","Organization","name",domain),
                "url",domain+"/"+url
        ));
        map.put("sku",productEntity.getSku());
        return SpringUtils.convertObjectToJson(map).replace("\n","").replace("\t","");
    }

    @Override
    public List<String> getDNS_PREFETCH() {
        return List.of("//caysenda.vn");
    }

    @Override
    public String descriptionDefault(WebsiteInfo websiteInfo) {
        if (websiteInfo.getDescription()!=null) return websiteInfo.getDescription();
        return "Cây sen đá chuyên cung cấp các loại sản phẩm víp pro";
    }

    @Override
    public void createSitemap() throws FileNotFoundException {
        String domain = env.getProperty("spring.domain");
        URL resource = ResourceUtils.getURL("classpath:static/sitemap");
        String path = resource.getPath();
        File sitemapDir = new File(path);
        if (!sitemapDir.exists()) {
            sitemapDir.mkdir();
        }
        Serializer serializer = new Persister();
        /**
         * Create index sitemap
         * */
        List<String> sitemaps = List.of("category-sitemap.xml", "page-sitemap.xml","product-sitemap.xml");
        SitemapIndex sitemapIndexData = new SitemapIndex();
        Date dateIndexSitemap = new Date();
        for (String s : sitemaps) {
            Sitemap sitemap = new Sitemap();
            sitemap.setLastmod(dateIndexSitemap);
            sitemap.setLoc(env.getProperty("spring.domain")+"/resources/sitemap/" + s);
            if (sitemapIndexData.getSitemap() == null) {
                List<Sitemap> list = new ArrayList<>();
                list.add(sitemap);
                sitemapIndexData.setSitemap(list);
            } else {
                sitemapIndexData.getSitemap().add(sitemap);
            }
        }
        try {
            File sitemapIndexFile = new File(path + "/sitemap_index_temp.xml");
            if (!sitemapIndexFile.exists()) {
                sitemapIndexFile.createNewFile();
            }
            serializer.write(sitemapIndexData, sitemapIndexFile);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapIndexFile));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/sitemap-index.xml")));
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + env.getProperty("spring.domain") + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(inputStream.readAllBytes());
            inputStream.close();
            outputStream.close();
            sitemapIndexFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Create product sitemap
         * */

        List<ProductDTO> products = productRepository.findAllForProductDTO(env.getProperty("spring.domain"));
        if (products.size() > 500) {
            Integer totalPage = products.size() / 500;
            if (totalPage * 500 < products.size()) {
                totalPage = totalPage + 1;
            }
            SitemapIndex sitemapIndexProduct = new SitemapIndex();
            Date datesitemapIndexProduct = new Date();
            for (int i = 1; i <= totalPage; i++) {
                Sitemap sitemap = new Sitemap();
                sitemap.setLastmod(datesitemapIndexProduct);
                sitemap.setLoc(domain+"/resources/sitemap/product-sitemap-" + i + ".xml");
                if (sitemapIndexProduct.getSitemap() == null) {
                    List<Sitemap> list = new ArrayList<>();
                    list.add(sitemap);
                    sitemapIndexProduct.setSitemap(list);
                } else {
                    sitemapIndexProduct.getSitemap().add(sitemap);
                }
                /**
                 *
                 * */
                SitemapUrlSet urlSet = new SitemapUrlSet();
                Date date = new Date();
                for (int j = (i - 1) * 500; j < (i - 1) * 500 + 500; j++) {
                    if (j >= products.size()) {
                        break;
                    }
                    SitemapUrl url = new SitemapUrl();
                    url.setLoc(domain+"/"+products.get(j).getCategorySlug()+"/"+products.get(j).getSlug());
                    url.setLastmod(date);
                    url.setPriority((float) 0.8);

                    if (products.get(j)!=null && products.get(j).getGallery()!=null && products.get(j).getGallery().size() > 0) {
                        List<SitemapImage> images = new ArrayList<>();
                        for (String dto : products.get(j).getGallery()) {
                            SitemapImage sitemapImage = new SitemapImage();
                            sitemapImage.setTitle(products.get(j).getName());
                            sitemapImage.setCaption(products.get(j).getSlug());
                            sitemapImage.setLoc(env.getProperty("spring.domain") + "/resources/" + dto.replace("\\", "/"));
                            images.add(sitemapImage);
                        }
                        url.setImages(images);
                    }
                    if (urlSet.getUrls() == null) {
                        List<SitemapUrl> urls = new ArrayList<>();
                        urls.add(url);
                        urlSet.setUrls(urls);
                    } else {
                        urlSet.getUrls().add(url);
                    }
                }
                try {
                    File sitemapProduct = new File(path + "/product-sitemap-" + i + "-temp.xml");
                    if (!sitemapProduct.exists()) {
                        sitemapProduct.createNewFile();
                    }

                    serializer.write(urlSet, sitemapProduct);
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapProduct));
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/product-sitemap-" + i + ".xml")));
                    outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
                    outputStream.write("\n".getBytes());
                    outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + env.getProperty("spring.domain") + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
                    outputStream.write("\n".getBytes());
                    outputStream.write(inputStream.readAllBytes());
                    inputStream.close();
                    outputStream.close();
                    sitemapProduct.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /**
                 *
                 * */
            }
            try {
                File sitemapProduct = new File(path + "/product-sitemap-temp.xml");
                if (!sitemapProduct.exists()) {
                    sitemapProduct.createNewFile();
                }
                serializer.write(sitemapIndexProduct, sitemapProduct);
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapProduct));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/product-sitemap.xml")));
                outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + env.getProperty("spring.domain") + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(inputStream.readAllBytes());
                inputStream.close();
                outputStream.close();
                sitemapProduct.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SitemapUrlSet urlSet = new SitemapUrlSet();
            Date date = new Date();
            for (ProductDTO productDTO : products) {
                SitemapUrl url = new SitemapUrl();
                url.setLoc(domain+"/"+productDTO.getCategorySlug()+"/"+productDTO.getSlug());
                url.setLastmod(date);
                url.setPriority((float) 0.8);
                if (productDTO.getGallery().size() > 0) {
                    List<SitemapImage> images = new ArrayList<>();
                    for (String dto : productDTO.getGallery()) {
                        SitemapImage sitemapImage = new SitemapImage();
                        sitemapImage.setTitle(productDTO.getName());
                        sitemapImage.setCaption(productDTO.getName());
                        sitemapImage.setLoc(env.getProperty("spring.domain") + "/resources" + dto.replace("\\", "/"));
                        images.add(sitemapImage);
                    }
                    url.setImages(images);
                }
                if (urlSet.getUrls() == null) {
                    List<SitemapUrl> urls = new ArrayList<>();
                    urls.add(url);
                    urlSet.setUrls(urls);
                } else {
                    urlSet.getUrls().add(url);
                }
            }
            try {
                File sitemapProduct = new File(path + "/product-sitemap-temp.xml");
                if (!sitemapProduct.exists()) {
                    sitemapProduct.createNewFile();
                }
                serializer.write(urlSet, sitemapProduct);
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapProduct));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/product-sitemap.xml")));
                outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + env.getProperty("spring.domain") + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(inputStream.readAllBytes());
                inputStream.close();
                outputStream.close();
                sitemapProduct.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * Create category sitemap
         * */
        List<CategoryDTO> categoryEntities = categoryService.findAllByDomainForCategoryDTO();
        SitemapUrlSet urlSetCategory = new SitemapUrlSet();
        Date date1 = new Date();
        for (CategoryDTO categoryEntity:categoryEntities){
            SitemapUrl url = new SitemapUrl();
            url.setLoc(domain+categoryEntity.getSlug());
            url.setLastmod(date1);
            url.setPriority((float) 1);
            if (urlSetCategory.getUrls() == null) {
                List<SitemapUrl> urls = new ArrayList<>();
                urls.add(url);
                urlSetCategory.setUrls(urls);
            } else {
                urlSetCategory.getUrls().add(url);
            }
        }
        try {
            File sitemapCategory = new File(path + "/category-sitemap-temp.xml");
            if (!sitemapCategory.exists()) {
                sitemapCategory.createNewFile();
            }
            serializer.write(urlSetCategory, sitemapCategory);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapCategory));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/category-sitemap.xml")));
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + domain + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(inputStream.readAllBytes());
            inputStream.close();
            outputStream.close();
            sitemapCategory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Create page sitemap
         * */
        List<PageEntity> pages = pageService.findAllByDomain(domain);
        Date datePage = new Date();

        /**
         *
         * */
        SitemapUrlSet urlSetPage = new SitemapUrlSet();
        for (PageEntity pageDTO : pages) {
            SitemapUrl url = new SitemapUrl();
            if (pageDTO.getSlug().equals("/")){
                url.setLoc(domain);
            }else {
                url.setLoc(domain+"/"+pageDTO.getSlug());
            }

            url.setLastmod(datePage);
            url.setPriority((float) 1);
            if (urlSetPage.getUrls() == null) {
                List<SitemapUrl> urls = new ArrayList<>();
                urls.add(url);
                urlSetPage.setUrls(urls);
            } else {
                urlSetPage.getUrls().add(url);
            }
        }
        try {
            File sitemapPage = new File(path + "/page-sitemap-temp.xml");
            if (!sitemapPage.exists()) {
                sitemapPage.createNewFile();
            }
            serializer.write(urlSetPage, sitemapPage);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sitemapPage));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/page-sitemap.xml")));
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(("<?xml-stylesheet type=\"text/xsl\" href=\"" + domain + "/resources/sitemap/main-sitemap.xsl\"?>").getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(inputStream.readAllBytes());
            inputStream.close();
            outputStream.close();
            sitemapPage.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
