package com.nomi.caysenda.ghn.model.setting;


import java.util.List;

public class GHNSetttingModel {
    List<GHNTokenModel> tokens;
    String tokenDefault;

    public List<GHNTokenModel> getTokens() {
        return tokens;
    }

    public void setTokens(List<GHNTokenModel> tokens) {
        this.tokens = tokens;
    }

    public String getTokenDefault() {
        return tokenDefault;
    }

    public void setTokenDefault(String tokenDefault) {
        this.tokenDefault = tokenDefault;
    }
}
