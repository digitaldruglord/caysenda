package com.nomi.caysenda.utils;

import org.apache.commons.math3.util.Precision;

public class NumberUtils {
    public static Long round(Long price) {
        Integer placee = 1;
        if (price >= 20000) {
            placee = 4;
        } else if (price <= 1000) {
            placee = 2;
        } else {
            placee = 3;
        }
        Double scale = Math.pow(10, placee);
        Double round = Precision.round((price / scale), 1);
        Double result = round * scale;
        return result.longValue();
    }
}
