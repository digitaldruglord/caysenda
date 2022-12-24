package com.nomi.caysenda.facebook.models;

public class LongLiveAccessTokenRequest {
    String grant_type;
    String client_id;
    String client_secret;
    String fb_exchange_token;

    public LongLiveAccessTokenRequest() {
    }

    public LongLiveAccessTokenRequest(String fb_exchange_token) {
        this.fb_exchange_token = fb_exchange_token;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getFb_exchange_token() {
        return fb_exchange_token;
    }

    public void setFb_exchange_token(String fb_exchange_token) {
        this.fb_exchange_token = fb_exchange_token;
    }
}
