package com.nomi.caysenda.ghn.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.entity.GroupVariantEntity;

import javax.persistence.AttributeConverter;
import java.util.List;

public class GHNOrderItemConvert implements AttributeConverter<List<GHNOrderItemEntity>,String> {
    @Override
    public String convertToDatabaseColumn(List<GHNOrderItemEntity> ghnOrderItemEntities) {
        if (ghnOrderItemEntities!=null){
            try {
                return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(ghnOrderItemEntities);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<GHNOrderItemEntity> convertToEntityAttribute(String s) {
        if (s!=null){
            try {
                return new ObjectMapper().readValue(s,List.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
