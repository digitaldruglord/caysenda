package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminSettingAPI;

import com.nomi.caysenda.options.OptionKeys;
import com.nomi.caysenda.options.model.*;
import com.nomi.caysenda.services.OptionService;
import com.nomi.caysenda.services.SEOService;
import com.nomi.caysenda.services.SettingService;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/setting")
public class AdminSettingAPIImpl implements AdminSettingAPI {
    @Autowired
    @Qualifier("optionService")
    OptionService optionService;
    @Autowired
    SettingService settingService;
    @Autowired
    SEOService seoService;
    @Override
    public ResponseEntity<Map> getSettingSlideHome() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",settingService.findAllSlideHome());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateSettingSlideHome(SlideHome requestData) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.createSlideHome(requestData);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> deleteSettingSlideHome(Integer id) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.deleteSlideHome(id);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getSettingBannerTOP() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",settingService.findAllBannerTop());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateSettingBannerTOP(BannerTOP requestData) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.createBannerTop(requestData);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> deleteSettingBannerTOP(Integer id) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.deleteBannerTOP(id);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getSettingBanner() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",settingService.findAllBanner());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateSettingBanner(BannerTOP requestData) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.createBanner(requestData);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> deleteSettingBanner(Integer id) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.deleteBanner(id);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getSettingMenu() {
        Map map = new HashMap();
        List<Map> maps = new ArrayList<>();
        maps.add(Map.of("data",settingService.findAllMenu(OptionKeys.NAVCATEGORY),"key",OptionKeys.NAVCATEGORY));
        maps.add(Map.of("data",settingService.findAllMenu(OptionKeys.NAVTOP),"key",OptionKeys.NAVTOP));
        maps.add(Map.of("data",settingService.findAllMenu(OptionKeys.NAVFOOTER),"key",OptionKeys.NAVFOOTER));
        maps.add(Map.of("data",settingService.findAllMenu(OptionKeys.NAVFOOTER1),"key",OptionKeys.NAVFOOTER1));
        maps.add(Map.of("data",settingService.findAllMenu(OptionKeys.NAVFOOTER2),"key",OptionKeys.NAVFOOTER2));
        map.put("success",true);
        map.put("data",maps);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateSettingMenu(String key, List<MenuOption> menus) {
        Map map = new HashMap();
        settingService.createMenu(menus, key);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> deleteSettingMenu(String optionkey) {
        return null;
    }

    @Override
    public ResponseEntity<Map> getWebsite() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",settingService.getWebsite());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateWebsite(WebsiteInfo websiteInfo) {
        Map map = new HashMap();
        map.put("success",true);
        settingService.updateWebsite(websiteInfo);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getEmailSetting() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getEmail()));
    }

    @Override
    public ResponseEntity<Map> updateEmailSetting(EmailSetting emailSetting) {
        settingService.updateEmail(emailSetting);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getPriceSettting() {
        PriceOption priceOption = settingService.getPriceSetting();
        return ResponseEntity.ok(Map.of("success",true,"data",priceOption==null?"":priceOption));
    }

    @Override
    public ResponseEntity<Map> updatePriceSetting(PriceOption priceOption) {
        settingService.updatePriceSetting(priceOption);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> shipSettting() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getShipseting()));
    }

    @Override
    public ResponseEntity<Map> updateShipSetting(ShipSetting shipSetting) {
        settingService.updateShipSetting(shipSetting);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> brandsSetting() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getBrands()));
    }

    @Override
    public ResponseEntity<Map> updateBrandsSetting(List<BrandOption> brandOptions) {
        settingService.updateBrands(brandOptions);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> embedSetting() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getEmbedHeader()));
    }

    @Override
    public ResponseEntity<Map> updateEmbedSetting(List<EmbedHeader> embedHeaders) {
        settingService.updateEmbedHeader(embedHeaders);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> embedSocial() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getEmbedSocial()));
    }

    @Override
    public ResponseEntity<Map> updateSocialSetting(List<EmbedSocial> embedSocials) {
        settingService.updateEmbedSocial(embedSocials);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getRobots() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getRobots()));
    }

    @Override
    public ResponseEntity<Map> updateRobots(Robots robots) {
        settingService.updateRobots(robots);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Map> createSiteMap() throws FileNotFoundException {
        seoService.createSitemap();
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getFacebookSetting() {
        Map map = new HashMap();
        map.put("success",true);
        FacebookSetting facebookSetting = settingService.getFacebook();
        if (facebookSetting!=null){
            map.put("data",facebookSetting);
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateFacebookSetting(FacebookSetting facebookSetting) {

        settingService.updateFacebookSetting(facebookSetting);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteFacebookSetting() {
        settingService.deleteFacebookSetting();
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getGeo2IP() {
        return ResponseEntity.ok(Map.of("success",true,"data",settingService.getGeo2IP()));
    }

    @Override
    public ResponseEntity<Map> updateGeo2IP(Geo2IP geo2IP) {
        settingService.updateGeo2IP(geo2IP);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteGeo2IP() {
        settingService.deleteGeo2IP();
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getDeliverySetting() {

        return ResponseEntity.ok(Map.of("success",true,"data",settingService.delivery()));
    }

    @Override
    public ResponseEntity<Map> updateDeliverySetting(Map map) {
        settingService.updateDelivery(map);
        return ResponseEntity.ok(Map.of("success",true));
    }
}
