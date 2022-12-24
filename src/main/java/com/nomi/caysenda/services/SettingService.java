package com.nomi.caysenda.services;

import com.nomi.caysenda.options.model.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface SettingService {
    /** slide */
    @CacheEvict(value = "slideSetting",allEntries = true)
    void createSlideHome(SlideHome slideHome);
    @Cacheable(value = "slideSetting")
    List<SlideHome> findAllSlideHome();
    @CacheEvict(value = "slideSetting",allEntries = true)
    void deleteSlideHome(Integer id);
    /** end slide */
    /** banner top*/
    @CacheEvict(value = "bannerSetting",allEntries = true)
    void createBannerTop(BannerTOP bannerTOP);
    @Cacheable("bannerSetting")
    List<BannerTOP> findAllBannerTop();
    @CacheEvict(value = "bannerSetting",allEntries = true)
    void deleteBannerTOP(Integer id);
    /** end banner top*/
    /** banner top*/
    void createBanner(BannerTOP bannerTOP);
    List<BannerTOP> findAllBanner();
    void deleteBanner(Integer id);
    /** end banner top*/
    /** menu
     * @param menus
     * @param key*/
    @CacheEvict(value = "menuSetting",key = "#key",allEntries = true)
    void createMenu(List<MenuOption> menus,String key);
    @Cacheable(value = "menuSetting",key = "#key")
    List<MenuOption> findAllMenu(String key);
    /** end menu*/
    /** Website*/
    @Cacheable(value = "websiteSetting")
    WebsiteInfo getWebsite();
    @CacheEvict(value = "websiteSetting",allEntries = true)
    void updateWebsite(WebsiteInfo websiteInfo);
    /** End website*/
    /** Email setting*/
    EmailSetting getEmail();
    void updateEmail(EmailSetting emailSetting);
    /** End Email setting*/
    /** Price Setting */
    @Cacheable(value = "priceSetting")
    PriceOption getPriceSetting();
    @CacheEvict(value = "priceSetting",allEntries = true)
    void updatePriceSetting(PriceOption priceOption);
    String getSortField();
    /** End Price Setting */
    /**
     * Ship setting
     * */
    ShipSetting getShipseting ();
    void updateShipSetting(ShipSetting shipSetting);
    /**
     * End ship setting
     * */
    /**
     * brand setting
     * */
    @Cacheable(value = "brandSetting")
    List<BrandOption> getBrands ();
    @CacheEvict(value = "brandSetting",allEntries = true)
    void updateBrands(List<BrandOption> brandOptions);
    /**
     * End brand setting
     * */
    /**
     * embedHeader setting
     * */
    @Cacheable(value = "embedHeaderSetting")
    List<EmbedHeader> getEmbedHeader ();
    @CacheEvict(value = "embedHeaderSetting",allEntries = true)
    void updateEmbedHeader(List<EmbedHeader> embedHeaderList);
    /**
     * End embedHeader setting
     * */
    /**
     * embedHeader setting
     * */
    @Cacheable(value = "embedSocial")
    List<EmbedSocial> getEmbedSocial ();
    @CacheEvict(value = "embedSocial",allEntries = true)
    void updateEmbedSocial(List<EmbedSocial> embedSocials);
    /**
     * End embedHeader setting
     * */
    /**
     * Robots setting
     * */
    Robots getRobots ();
    void updateRobots(Robots robots);
    /**
     * End Robots setting
     * */
    /** facebook */
    FacebookSetting getFacebook();
    void updateFacebookSetting(FacebookSetting facebookSetting);
    void deleteFacebookSetting();
    /**
     * End Robots setting
     * */
    /** facebook */
    @Cacheable(value = "geo2ip")
    Geo2IP getGeo2IP();
    @CacheEvict(value = "geo2ip",allEntries = true)
    void updateGeo2IP(Geo2IP geo2IP);
    void deleteGeo2IP();
    /* end facebook*/
    /** lazada */
    LazadaSetting getLazadaSetting();
    void  updateLazadaSetting(LazadaSetting lazadaSetting);
    Map delivery();
    void updateDelivery(Map map);

}
