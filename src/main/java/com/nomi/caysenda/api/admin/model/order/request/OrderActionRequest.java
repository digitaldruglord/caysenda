package com.nomi.caysenda.api.admin.model.order.request;

import java.util.List;

public class OrderActionRequest {
    String action;
    List<Integer> ids;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
