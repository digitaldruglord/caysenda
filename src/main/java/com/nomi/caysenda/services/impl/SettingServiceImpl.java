package com.nomi.caysenda.services.impl;

import com.google.common.reflect.TypeToken;
import com.nomi.caysenda.facebook.models.LongLiveAccessTokenRequest;
import com.nomi.caysenda.facebook.models.LongLiveAccessTokenResponse;
import com.nomi.caysenda.facebook.services.FacebookServiceAPI;
import com.nomi.caysenda.options.OptionKeys;
import com.nomi.caysenda.options.model.*;
import com.nomi.caysenda.services.OptionService;
import com.nomi.caysenda.services.SettingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class SettingServiceImpl implements SettingService {
    @Autowired
    OptionService optionService;
    @Autowired
    FacebookServiceAPI facebookServiceAPI;
    @Override
    public void createSlideHome(SlideHome slideHome) {
        List<SlideHome> slideHomes = findAllSlideHome();
        if (slideHome.getId()!=null){
            SlideHome slideEntity = slideHomes.stream().filter((slideHome1 -> slideHome1.getId().equals(slideHome.getId()))).findAny().orElse(null);
            if (slideEntity!=null){
                slideEntity.setPrice(slideHome.getPrice());
                slideEntity.setThumbnail(slideHome.getThumbnail());
                slideEntity.setDescription(slideHome.getDescription());
                slideEntity.setHref(slideHome.getHref());
                slideEntity.setTitle(slideHome.getTitle());
            }else {
                slideHomes.add(slideHome);
            }
        }else {
            slideHomes.add(slideHome);
        }
        saveSlideHome(slideHomes);
    }
    private void saveSlideHome(List<SlideHome> slideHomes){
        int i = 1;
        for (SlideHome slideHome:slideHomes){
            slideHome.setId(i++);
        }
        optionService.update(slideHomes,OptionKeys.SLIDEHOME);
    }

    @Override
    public List<SlideHome> findAllSlideHome() {
        List list = optionService.getData(OptionKeys.SLIDEHOME,List.class);
        if (list==null) return new ArrayList<SlideHome>();
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<SlideHome>>(){}.getType();
        return mapper.map(list,listType);
    }

    @Override
    public void deleteSlideHome(Integer id) {
        List<SlideHome> slideHomes = findAllSlideHome();
        Iterator<SlideHome> iterator = slideHomes.listIterator();
        while (iterator.hasNext()){
            SlideHome slideHome = iterator.next();
            if (slideHome.getId().equals(id)){
                iterator.remove();
            }
        }
        saveSlideHome(slideHomes);
    }

    @Override
    public void createBannerTop(BannerTOP bannerTOP) {
        List<BannerTOP> banners = findAllBannerTop();
        if (bannerTOP.getId()!=null){
            BannerTOP  bannerTOP2 = banners.stream().filter((bannerTOP1 -> bannerTOP1.getId().equals(bannerTOP.getId()))).findAny().orElse(null);
            if (bannerTOP2!=null){
                bannerTOP2.setContent(bannerTOP.getContent());
                bannerTOP2.setThumbnail(bannerTOP.getThumbnail());
                bannerTOP2.setHref(bannerTOP.getHref());
            }else {
                banners.add(bannerTOP);
            }
        }else {
            banners.add(bannerTOP);
        }
        saveBannerTOP(banners);
    }
    private void saveBannerTOP(List<BannerTOP> banners){
        int i = 1;
        for (BannerTOP bannerTOP:banners){
            bannerTOP.setId(i++);

        }
        optionService.update(banners,OptionKeys.BANNERTOP);
    }

    @Override
    public List<BannerTOP> findAllBannerTop() {
        List list = optionService.getData(OptionKeys.BANNERTOP,List.class);
        if (list==null) return new ArrayList<BannerTOP>();
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<BannerTOP>>(){}.getType();
        return mapper.map(list,listType);
    }

    @Override
    public void deleteBannerTOP(Integer id) {
        List<BannerTOP> bannerTOPS = findAllBannerTop();
        Iterator<BannerTOP> iterator = bannerTOPS.listIterator();
        while (iterator.hasNext()){
            BannerTOP bannerTOP = iterator.next();
            if (bannerTOP.getId().equals(id)){
                iterator.remove();
            }
        }
        saveBannerTOP(bannerTOPS);
    }

    private void saveBanner(List<BannerTOP> banners){
        int i = 1;
        for (BannerTOP bannerTOP:banners){
            bannerTOP.setId(i++);
        }
        optionService.update(banners,OptionKeys.BANNER);
    }
    @Override
    public void createBanner(BannerTOP bannerTOP) {
        List<BannerTOP> banners = findAllBanner();
        if (bannerTOP.getId()!=null){
            BannerTOP  bannerTOP2 = banners.stream().filter((bannerTOP1 -> bannerTOP1.getId().equals(bannerTOP.getId()))).findAny().orElse(null);
            if (bannerTOP2!=null){
                bannerTOP2.setContent(bannerTOP.getContent());
                bannerTOP2.setThumbnail(bannerTOP.getThumbnail());
                bannerTOP2.setHref(bannerTOP.getHref());
            }else {
                banners.add(bannerTOP);
            }
        }else {
            banners.add(bannerTOP);
        }
        saveBanner(banners);
    }

    @Override
    public List<BannerTOP> findAllBanner() {
        List list = optionService.getData(OptionKeys.BANNER,List.class);
        if (list==null) return new ArrayList<BannerTOP>();
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<BannerTOP>>(){}.getType();
        return mapper.map(list,listType);
    }

    @Override
    public void deleteBanner(Integer id) {
        List<BannerTOP> bannerTOPS = findAllBannerTop();
        Iterator<BannerTOP> iterator = findAllBanner().listIterator();
        while (iterator.hasNext()){
            BannerTOP bannerTOP = iterator.next();
            if (bannerTOP.getId().equals(id)){
                iterator.remove();
            }
        }
        saveBanner(bannerTOPS);
    }

    @Override
    public void createMenu(List<MenuOption> menus, String key) {
        Integer i = 1;
        for (MenuOption menuOption : menus) {
            menuOption.setId(i++);
           if (menuOption.getChildren()!=null){
               for (MenuOption child:menuOption.getChildren()){
                   child.setId(i++);
                   if (child.getChildren()!=null){
                       for (MenuOption child1:child.getChildren()){
                           child1.setId(i++);
                           if (child1.getChildren()!=null){
                               for (MenuOption child2:child1.getChildren()){
                                   child2.setId(i++);
                               }
                           }
                       }
                   }
               }
           }
        }
        optionService.update(menus,key);
    }

    @Override
    public List<MenuOption> findAllMenu(String key) {
        List list = optionService.getData(key,List.class);
        if (list==null) return new ArrayList<MenuOption>();
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<MenuOption>>(){}.getType();
        return mapper.map(list,listType);
    }

    @Override
    public WebsiteInfo getWebsite() {
        return optionService.getData(OptionKeys.WEBSITEINFO,WebsiteInfo.class);
    }

    @Override
    public void updateWebsite(WebsiteInfo websiteInfo) {
        WebsiteInfo info = getWebsite();
        if (info!=null){
            websiteInfo.setEmbedList(info.getEmbedList());
            websiteInfo.setEmbedSocials(info.getEmbedSocials());
        }
        optionService.update(websiteInfo,OptionKeys.WEBSITEINFO);
    }

    @Override
    public EmailSetting getEmail() {
        EmailSetting emailSetting = optionService.getData(OptionKeys.EMAIL_SETTING,EmailSetting.class);
        if (emailSetting!=null){
            if (emailSetting.getContents().get("pendding")==null){
                Map map = emailSetting.getContents();
                map.put("pendding","Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
                emailSetting.setContents(map);
            }
            return emailSetting;
        }else {
            emailSetting = new EmailSetting();
            emailSetting.setEmail("lakdak4@gmail.com");
            emailSetting.setPassword("");
            Map contents = new HashMap();
            contents.put("cancel", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("success", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("processing", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("failed", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("shipping", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("awaitingadditionaldelivery", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("partiallypaid", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            contents.put("paid", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!n");
            contents.put("refunded", "Đơn hàng #%s của bạn đã được đóng gói và chuyển cho bên vận chuyển Bạn sẽ nhận được hàng trong thời gian sớm nhất Từ (1 đến 3 ngày) Bạn Cần Hỗ Trợ hãy liên hệ qua : Hotlinze/Zalo: 0971806636 Cảm ơn bạn đã tin tưởng đặt hàng trên websize!");
            emailSetting.setContents(contents);
            return emailSetting;
        }

    }

    @Override
    public void updateEmail(EmailSetting emailSetting) {
        optionService.update(emailSetting,OptionKeys.EMAIL_SETTING);
    }

    @Override
    public PriceOption getPriceSetting() {
        PriceOption priceOption = optionService.getData(OptionKeys.PRICE_SETTING,PriceOption.class);
        if (priceOption==null){
            priceOption = new PriceOption();
            priceOption.setPrice1("vip1");
            priceOption.setPrice2("vip2");
            priceOption.setPrice3("vip3");
            priceOption.setPriceDefault("default");
            priceOption.setEnableConditionCategory(false);
            priceOption.setEnableRetail(false);
        }
        if (priceOption.getLogin()==null) priceOption.setLogin(true);
        if (priceOption.getEnableConditionCategory()==null) priceOption.setEnableConditionCategory(false);
        if (priceOption.getEnableRetail()==null) priceOption.setEnableRetail(false);
        return priceOption;
    }

    @Override
    public void updatePriceSetting(PriceOption priceOption) {
        optionService.update(priceOption,OptionKeys.PRICE_SETTING);
    }

    @Override
    public String getSortField() {
        PriceOption priceOption = getPriceSetting();
        switch (priceOption.getPrice3()){
            case "vip1": return "minv1";
            case "vip2": return "minv2";
            case "vip3": return "minv3";
            case "default": return "minPrice";
        }
        return "minPrice";
    }

    @Override
    public ShipSetting getShipseting() {
        ShipSetting shipSetting = optionService.getData(OptionKeys.SHIP_SETTING,ShipSetting.class);
        if (shipSetting==null){
            shipSetting = new ShipSetting();
            shipSetting.setEnable(false);
            shipSetting.setFee(Long.valueOf(30000));
            shipSetting.setExtraFee(Long.valueOf(6000));
        }
        return shipSetting;
    }

    @Override
    public void updateShipSetting(ShipSetting shipSetting) {
        if (shipSetting.getFee()==null) shipSetting.setFee(Long.valueOf(0));
        if (shipSetting.getExtraFee()==null) shipSetting.setExtraFee(Long.valueOf(0));
        optionService.update(shipSetting,OptionKeys.SHIP_SETTING);
    }

    @Override
    public List<BrandOption> getBrands() {
        List list = optionService.getData(OptionKeys.TRADEMARK_SETTING,List.class);
        if (list==null) return new ArrayList<BrandOption>();
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<BrandOption>>(){}.getType();
        return mapper.map(list,listType);
    }

    @Override
    public void updateBrands(List<BrandOption> brandOptions) {
        optionService.update(brandOptions,OptionKeys.TRADEMARK_SETTING);
    }

    @Override
    public List<EmbedHeader> getEmbedHeader() {
        WebsiteInfo info = getWebsite();
        return info.getEmbedList()!=null?info.getEmbedList():new ArrayList<>();
    }

    @Override
    public void updateEmbedHeader(List<EmbedHeader> embedHeaderList) {
        WebsiteInfo info = getWebsite();
        info.setEmbedList(embedHeaderList);
        optionService.update(info,OptionKeys.WEBSITEINFO);
    }

    @Override
    public List<EmbedSocial> getEmbedSocial() {
        WebsiteInfo info = getWebsite();
        return info.getEmbedSocials()!=null?info.getEmbedSocials():new ArrayList<>();
    }

    @Override
    public void updateEmbedSocial(List<EmbedSocial> embedSocials) {
        WebsiteInfo info = getWebsite();
        info.setEmbedSocials(embedSocials);
        optionService.update(info,OptionKeys.WEBSITEINFO);
    }

    @Override
    public Robots getRobots() {
        Robots robots = optionService.getData(OptionKeys.ROBOTS_SETTING,Robots.class);
        if (robots==null){
            robots = new Robots();
            robots.setEnable(false);
        }
        return robots;
    }

    @Override
    public void updateRobots(Robots robots) {
        optionService.update(robots,OptionKeys.ROBOTS_SETTING);
    }

    @Override
    public FacebookSetting getFacebook() {
        FacebookSetting facebookSetting = optionService.getData(OptionKeys.FACEBOOK_SETTING,FacebookSetting.class);
        return facebookSetting;
    }

    @Override
    public void updateFacebookSetting(FacebookSetting facebookSetting) {
        optionService.update(facebookSetting,OptionKeys.FACEBOOK_SETTING);
        LongLiveAccessTokenResponse longLiveAccessTokenResponse = facebookServiceAPI.longLiveAccessToken(new LongLiveAccessTokenRequest(facebookSetting.getAccessToken()));
        if (longLiveAccessTokenResponse!=null){
            facebookSetting.setAccessToken(longLiveAccessTokenResponse.getAccess_token());
            facebookSetting.setExpireToken(longLiveAccessTokenResponse.getExpires_in());
        }

        optionService.update(facebookSetting,OptionKeys.FACEBOOK_SETTING);
    }

    @Override
    public void deleteFacebookSetting() {
        FacebookSetting facebookSetting = getFacebook();
        FacebookSetting resetData = new FacebookSetting();
        resetData.setContent(facebookSetting.getContent());
        updateFacebookSetting(resetData);
    }

    @Override
    public Geo2IP getGeo2IP() {
        Geo2IP geo2IP = optionService.getData(OptionKeys.GEO2IP,Geo2IP.class);
        if (geo2IP==null) {
            geo2IP = new Geo2IP();
            geo2IP.setEnable(false);
            geo2IP.setList(new ArrayList<>());
        }
        return geo2IP;
    }

    @Override
    public void updateGeo2IP(Geo2IP geo2IP) {
        optionService.update(geo2IP,OptionKeys.GEO2IP);
    }

    @Override
    public void deleteGeo2IP() {
        optionService.delete(OptionKeys.GEO2IP);
    }

    @Override
    public LazadaSetting getLazadaSetting() {
        LazadaSetting setting = optionService.getData(OptionKeys.LAZADA,LazadaSetting.class);
        if (setting==null){
            setting = new LazadaSetting();
            setting.setAppId(105234);
        }
        return setting;
    }

    @Override
    public void updateLazadaSetting(LazadaSetting lazadaSetting) {
        optionService.update(lazadaSetting,OptionKeys.LAZADA);
    }

    @Override
    public Map delivery() {
        Map map = optionService.getData(OptionKeys.DELIVERY,Map.class);
        if (map==null) map = new HashMap();
        return map;
    }

    @Override
    public void updateDelivery(Map map) {
        optionService.update(map,OptionKeys.DELIVERY);
    }
}
