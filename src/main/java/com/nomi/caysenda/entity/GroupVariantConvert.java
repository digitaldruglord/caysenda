package com.nomi.caysenda.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.dto.cart.CartProductDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Type;
import java.util.List;

public class GroupVariantConvert implements AttributeConverter<List<GroupVariantEntity>,String> {
    @Override
    public String convertToDatabaseColumn(List<GroupVariantEntity> groupVariantEntities) {
        if (groupVariantEntities!=null){
            try {
                return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(groupVariantEntities);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<GroupVariantEntity> convertToEntityAttribute(String s) {
        if (s!=null){
            try {
                List list = new ObjectMapper().readValue(s,List.class);
                Type listType = new TypeToken<List<GroupVariantEntity>>(){}.getType();
                return new ModelMapper().map(list,listType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
