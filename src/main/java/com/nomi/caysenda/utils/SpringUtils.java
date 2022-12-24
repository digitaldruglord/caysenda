package com.nomi.caysenda.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SpringUtils {
    public static ObjectMapper mapper = new ObjectMapper();
    public static String convertObjectToJson(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
    public static <T> T convertJsonToObject(String json,Class<T> object){
        if (json==null) return null;
        try {
            return mapper.readValue(json,object);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
    public static Map<String, Date> getDateBetween(String option, Integer year){
        Map<String,Date> map = new HashMap<>();
        Date fromDate = null,toDate = null;
        String regexMonth = "month\\d{1,2}";
        Pattern pattern = Pattern.compile(regexMonth);
        LocalDateTime currentDate = LocalDateTime.now();
        if (option==null) option = "currentMonth";
        if (year==null) year = currentDate.getYear();
        if (option.equalsIgnoreCase("today")){
            LocalDateTime minTimeInToday =  currentDate.with(LocalTime.MIN);
            fromDate = Date.from(minTimeInToday.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("yesterday")){
            LocalDateTime yesterday = LocalDateTime.now().minus(Period.ofDays(1));
            LocalDateTime minTimeInYesterday =  yesterday.with(LocalTime.MIN);
            fromDate = Date.from(minTimeInYesterday.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(yesterday.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("7daysago")){
            LocalDateTime _7daysago = LocalDateTime.now().minus(Period.ofDays(7)).with(LocalTime.MIN);
            fromDate = Date.from(_7daysago.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("30daysago")){
            LocalDateTime _30daysago = LocalDateTime.now().minus(Period.ofDays(30)).with(LocalTime.MIN);
            fromDate = Date.from(_30daysago.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("90daysago")){
            LocalDateTime _90daysago = LocalDateTime.now().minus(Period.ofDays(90)).with(LocalTime.MIN);
            fromDate = Date.from(_90daysago.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("earlyyear")){
            LocalDateTime earlyyear = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
            fromDate = Date.from(earlyyear.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(currentDate.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("q1")){
            LocalDateTime qfrom = LocalDateTime.of(year,1,1,0,0);
            LocalDateTime qto = qfrom.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            fromDate = Date.from(qfrom.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(qto.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("q2")){
            LocalDateTime q2from = LocalDateTime.of(year,4,1,0,0);
            LocalDateTime q2to = q2from.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            fromDate = Date.from(q2from.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(q2to.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("q3")){
            LocalDateTime q3from = LocalDateTime.of(year,7,1,0,0);
            LocalDateTime q3to = q3from.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            fromDate = Date.from(q3from.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(q3to.atZone(ZoneId.systemDefault()).toInstant());
        }else if (option.equalsIgnoreCase("q4")){
            LocalDateTime q4from = LocalDateTime.of(year,10,1,0,0);
            LocalDateTime q4to = q4from.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            fromDate = Date.from(q4from.atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(q4to.atZone(ZoneId.systemDefault()).toInstant());
        }else if (pattern.matcher(option).find()){
            Integer month = Integer.valueOf(option.substring((option.lastIndexOf("month")+"month".length())));
            if (month>=1 && month<=12){
                LocalDateTime dateTime = LocalDateTime.of(year,month,1,0,0,0);
                fromDate = Date.from(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
                toDate = Date.from(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
            }

        }else if (option.equalsIgnoreCase("currentMonth")){
            LocalDateTime dateTime = LocalDateTime.now();
            fromDate = Date.from(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
            toDate = Date.from(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        }
        map.put("from",fromDate);
        map.put("to",toDate);
        return map;
    }
    public static String getDomain(){
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return String.valueOf(requestAttributes.getAttribute("domain", RequestAttributes.SCOPE_SESSION));
    }
}
