package com.nomi.caysenda.controller.responses.address;

public class AddressDataResponse {
    String id;
    String name;

    public AddressDataResponse() {
    }

    public AddressDataResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
