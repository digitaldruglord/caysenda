package com.nomi.caysenda.ghn.service;

public interface GHNSettingService {
    <T> T getData(String key,Class<T> tClass);
    <T> void update(T t,String optionKey);
    void delete(String optionKey);
}
