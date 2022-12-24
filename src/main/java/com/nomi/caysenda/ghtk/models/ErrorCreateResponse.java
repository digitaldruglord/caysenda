package com.nomi.caysenda.ghtk.models;

public class ErrorCreateResponse {
    String code;
    String partner_id;
    String ghtk_label;
    String created;
    Integer status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getGhtk_label() {
        return ghtk_label;
    }

    public void setGhtk_label(String ghtk_label) {
        this.ghtk_label = ghtk_label;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
