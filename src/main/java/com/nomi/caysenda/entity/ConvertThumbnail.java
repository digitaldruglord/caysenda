package com.nomi.caysenda.entity;

import org.apache.commons.validator.routines.UrlValidator;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ConvertThumbnail  implements AttributeConverter<String,String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        return s;
    }

    @Override
    public String convertToEntityAttribute(String s) {
        UrlValidator urlValidator = new UrlValidator();
        if (s!=null){
            if (!urlValidator.isValid(s)){
                if (!s.equals("")){
                    return "https://caysenda.vn"+s;
                }
            }else {
                return s;
            }
        }
        return null;
    }
}
