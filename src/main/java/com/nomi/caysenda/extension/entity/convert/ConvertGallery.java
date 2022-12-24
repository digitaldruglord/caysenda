package com.nomi.caysenda.extension.entity.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ConvertGallery implements AttributeConverter<List<String>,String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if (strings!=null){
            try {
                return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(strings);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
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
