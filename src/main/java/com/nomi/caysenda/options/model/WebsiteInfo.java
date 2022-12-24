package com.nomi.caysenda.options.model;

import java.util.List;

public class WebsiteInfo {
    String title;
    String phoneNumber;
    String address;
    String logo;
    String facebook;
    String zalo;
    String email;
    String icon16;
    String icon32;
    String icon48;
    String bannerFooter;
    String description;
    String slogan;
    String backgroundSlide;
    List<EmbedHeader> embedList;
    List<EmbedSocial> embedSocials;
    String linkAIOS;
    String linkANDROID;
    String anounceDelivery;
    String anounceConfirmDelivery;


    public WebsiteInfo() {
    }

    public WebsiteInfo(String title, String phoneNumber, String address, String logo, String facebook, String zalo) {
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.logo = logo;
        this.facebook = facebook;
        this.zalo = zalo;
    }

    public String getAnounceConfirmDelivery() {
        return anounceConfirmDelivery;
    }

    public void setAnounceConfirmDelivery(String anounceConfirmDelivery) {
        this.anounceConfirmDelivery = anounceConfirmDelivery;
    }

    public String getAnounceDelivery() {
        return anounceDelivery;
    }

    public void setAnounceDelivery(String anounceDelivery) {
        this.anounceDelivery = anounceDelivery;
    }

    public String getLinkAIOS() {
        return linkAIOS;
    }

    public void setLinkAIOS(String linkAIOS) {
        this.linkAIOS = linkAIOS;
    }

    public String getLinkANDROID() {
        return linkANDROID;
    }

    public void setLinkANDROID(String linkANDROID) {
        this.linkANDROID = linkANDROID;
    }

    public List<EmbedSocial> getEmbedSocials() {
        return embedSocials;
    }

    public void setEmbedSocials(List<EmbedSocial> embedSocials) {
        this.embedSocials = embedSocials;
    }

    public List<EmbedHeader> getEmbedList() {
        return embedList;
    }

    public void setEmbedList(List<EmbedHeader> embedList) {
        this.embedList = embedList;
    }

    public String getBackgroundSlide() {
        return backgroundSlide;
    }

    public void setBackgroundSlide(String backgroundSlide) {
        this.backgroundSlide = backgroundSlide;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerFooter() {
        return bannerFooter;
    }

    public void setBannerFooter(String bannerFooter) {
        this.bannerFooter = bannerFooter;
    }

    public String getIcon16() {
        return icon16;
    }

    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    public String getIcon32() {
        return icon32;
    }

    public void setIcon32(String icon32) {
        this.icon32 = icon32;
    }

    public String getIcon48() {
        return icon48;
    }

    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getZalo() {
        return zalo;
    }

    public void setZalo(String zalo) {
        this.zalo = zalo;
    }
}
