package com.nomi.caysenda.ghn.model.responses;

public class DataPickShiftModelResponse {
    Integer id;
    String title;
    Long from_time;
    Long to_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFrom_time() {
        return from_time;
    }

    public void setFrom_time(Long from_time) {
        this.from_time = from_time;
    }

    public Long getTo_time() {
        return to_time;
    }

    public void setTo_time(Long to_time) {
        this.to_time = to_time;
    }
}
