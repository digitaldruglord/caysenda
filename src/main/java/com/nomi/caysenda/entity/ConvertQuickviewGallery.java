package com.nomi.caysenda.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.UrlValidator;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ConvertQuickviewGallery implements AttributeConverter<List<String>,String> {
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
                List<String> strings = new ObjectMapper().readValue(s,List.class);
                UrlValidator urlValidator = new UrlValidator();
                for (int i=0;i<strings.size();i++){
                    if (!urlValidator.isValid(strings.get(i))){
                        strings.set(i,"https://caysenda.vn"+strings.get(i));
                    }
                }
                return strings;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
