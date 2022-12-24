package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.options.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


public interface AdminSettingAPI {
    @GetMapping("/slide-home")
    ResponseEntity<Map> getSettingSlideHome();
    @PostMapping("/slide-home")
    ResponseEntity<Map> updateSettingSlideHome(@RequestBody SlideHome requestData);
    @DeleteMapping("/slide-home")
    ResponseEntity<Map> deleteSettingSlideHome(@RequestParam("id") Integer id);
    @GetMapping("/banner-top")
    ResponseEntity<Map> getSettingBannerTOP();
    @PostMapping("/banner-top")
    ResponseEntity<Map> updateSettingBannerTOP(@RequestBody BannerTOP requestData);
    @DeleteMapping("/banner-top")
    ResponseEntity<Map> deleteSettingBannerTOP(@RequestParam("id") Integer id);
    @GetMapping("/banner")
    ResponseEntity<Map> getSettingBanner();
    @PostMapping("/banner")
    ResponseEntity<Map> updateSettingBanner(@RequestBody BannerTOP requestData);
    @DeleteMapping("/banner")
    ResponseEntity<Map> deleteSettingBanner(@RequestParam("id") Integer id);
    @GetMapping("/menu")
    ResponseEntity<Map> getSettingMenu();
    @PostMapping("/menu")
    ResponseEntity<Map> updateSettingMenu(@RequestParam("key") String key,
                                          @RequestBody List<MenuOption> menus);
    @DeleteMapping("/menu")
    ResponseEntity<Map> deleteSettingMenu(@RequestParam("id") String optionkey);
    @GetMapping("/website")
    ResponseEntity<Map> getWebsite();
    @PostMapping("/website")
    ResponseEntity<Map> updateWebsite(@RequestBody WebsiteInfo websiteInfo);
    @GetMapping("/email")
    ResponseEntity<Map> getEmailSetting();
    @PostMapping("/email")
    ResponseEntity<Map> updateEmailSetting(@RequestBody EmailSetting emailSetting);
    @GetMapping("/price")
    ResponseEntity<Map> getPriceSettting();
    @PostMapping("/price")
    ResponseEntity<Map> updatePriceSetting(@RequestBody PriceOption priceOption);
    @GetMapping("/ship")
    ResponseEntity<Map> shipSettting();
    @PostMapping("/ship")
    ResponseEntity<Map> updateShipSetting(@RequestBody ShipSetting shipSetting);
    @GetMapping("/brand")
    ResponseEntity<Map> brandsSetting();
    @PostMapping("/brand")
    ResponseEntity<Map> updateBrandsSetting(@RequestBody List<BrandOption> brandOptions);
    @GetMapping("/embed")
    ResponseEntity<Map> embedSetting();
    @PostMapping("/embed")
    ResponseEntity<Map> updateEmbedSetting(@RequestBody List<EmbedHeader> embedHeaders);
    @GetMapping("/embed-social")
    ResponseEntity<Map> embedSocial();
    @PostMapping("/embed-social")
    ResponseEntity<Map> updateSocialSetting(@RequestBody List<EmbedSocial> embedSocials);
    @GetMapping("/robots")
    ResponseEntity<Map> getRobots();
    @PostMapping("/robots")
    ResponseEntity<Map> updateRobots(@RequestBody Robots robots);
    @GetMapping("/create-sitemap")
    ResponseEntity<Map> createSiteMap() throws FileNotFoundException;
    @GetMapping("/facebook")
    ResponseEntity<Map> getFacebookSetting();
    @PostMapping("/facebook")
    ResponseEntity<Map> updateFacebookSetting(@RequestBody FacebookSetting facebookSetting);
    @DeleteMapping("/facebook")
    ResponseEntity<Map> deleteFacebookSetting();
    @GetMapping("/geo2ip")
    ResponseEntity<Map> getGeo2IP();
    @PostMapping("/geo2ip")
    ResponseEntity<Map> updateGeo2IP(@RequestBody Geo2IP geo2IP);
    @DeleteMapping("/geo2ip")
    ResponseEntity<Map> deleteGeo2IP();
    @GetMapping("/delivery")
    ResponseEntity<Map> getDeliverySetting();
    @PostMapping("/delivery")
    ResponseEntity<Map> updateDeliverySetting(@RequestBody Map map);
}
