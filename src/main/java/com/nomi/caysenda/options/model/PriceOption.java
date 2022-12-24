package com.nomi.caysenda.options.model;

public class PriceOption {
    String price1;
    String price2;
    String price3;
    String priceDefault;
    Boolean enableConditionCategory;
    Boolean enableRetail;
    Boolean login;

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public Boolean getEnableConditionCategory() {
        return enableConditionCategory;
    }

    public void setEnableConditionCategory(Boolean enableConditionCategory) {
        this.enableConditionCategory = enableConditionCategory;
    }

    public Boolean getEnableRetail() {
        return enableRetail;
    }

    public void setEnableRetail(Boolean enableRetail) {
        this.enableRetail = enableRetail;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public String getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(String priceDefault) {
        this.priceDefault = priceDefault;
    }

}
