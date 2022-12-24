package com.nomi.caysenda.utils;



import java.text.Normalizer;
import java.util.regex.Pattern;

public class ConvertStringToUrl {
    public static String covertStringToURL(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp)
                    .replaceAll("").toLowerCase().trim()
                    .replaceAll("\\s{2,}", "-")
                    .replaceAll("\\.", "")
                    .replaceAll("/", "")
                    .replaceAll(" ", "-")
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll("đ", "d")
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .replaceAll(":" ,"-")
                    .replaceAll("（" ,"")
                    .replaceAll("）" ,"");
        } catch (Exception e) {

        }
        return "";
    }
    public static String covertNameFileToUrl(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp)
                    .replaceAll("").trim()
                    .replaceAll("\\s{2,}", "-")
                    .replaceAll(" ", "-")
                    .replaceAll("đ", "d")
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .replaceAll(":" ,"-")

                    ;
        } catch (Exception e) {

        }
        return "";
    }

}
