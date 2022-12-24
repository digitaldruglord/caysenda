package com.nomi.caysenda.extension.model.request;

import java.util.List;

public class ExtensionBulkActionRequest {
    List<Integer> ids;
    String event;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
