package com.nomi.caysenda.entity;

import com.nomi.caysenda.utils.SpringUtils;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.Map;

public class ConvertMapToString implements AttributeConverter<Map,String> {

    @Override
    public String convertToDatabaseColumn(Map map) {

        return SpringUtils.convertObjectToJson(map);
    }

    @Override
    public Map convertToEntityAttribute(String s) {
        return SpringUtils.convertJsonToObject(s,Map.class);
    }
}
