package com.nomi.caysenda.options.model;

public class FacebookSetting {
    String userId;
    String accessToken;
    String email;
    String name;
    Long expireToken;
    String pageId;
    String pageAccessToken;
    Long expirePageToken;
    String pageName;
    String content;
    String appId;
    String appSecrect;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecrect() {
        return appSecrect;
    }

    public void setAppSecrect(String appSecrect) {
        this.appSecrect = appSecrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExpireToken() {
        return expireToken;
    }

    public void setExpireToken(Long expireToken) {
        this.expireToken = expireToken;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageAccessToken() {
        return pageAccessToken;
    }

    public void setPageAccessToken(String pageAccessToken) {
        this.pageAccessToken = pageAccessToken;
    }

    public Long getExpirePageToken() {
        return expirePageToken;
    }

    public void setExpirePageToken(Long expirePageToken) {
        this.expirePageToken = expirePageToken;
    }
}
