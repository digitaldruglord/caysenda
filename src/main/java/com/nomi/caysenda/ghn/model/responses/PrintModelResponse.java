package com.nomi.caysenda.ghn.model.responses;

public class PrintModelResponse extends GHNBaseResponse {
    DataPrintResponse data;
    String template;

    public DataPrintResponse getData() {
        return data;
    }

    public void setData(DataPrintResponse data) {
        this.data = data;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
