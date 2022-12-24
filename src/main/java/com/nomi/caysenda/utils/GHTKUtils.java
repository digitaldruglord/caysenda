package com.nomi.caysenda.utils;


import com.nomi.caysenda.ghtk.models.PickModelGHTK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GHTKUtils {
    public static List<PickModelGHTK> convertResponsePickToModelGHTK(List<Map<String, Object>> data){
        List<PickModelGHTK> listPick = new ArrayList<>();
        if (data!=null){
            for (Map<String, Object> map:data){
                PickModelGHTK pickModelGHTK = null;
                String pickAddressId = (String) map.get("pick_address_id");
                String address =(String) map.get("address");
                String pick_tel = (String) map.get("pick_tel");
                String pick_name = (String) map.get("pick_name");
                pickModelGHTK =  splitAddress(address);
                if (pickModelGHTK!=null){
                    pickModelGHTK.setPick_address_id(pickAddressId);
                    pickModelGHTK.setPick_name(pick_name);
                    pickModelGHTK.setPick_tel(pick_tel);
                    listPick.add(pickModelGHTK);
                }
            }
        }
        return listPick;
    }
    public static PickModelGHTK splitAddress(String address){
        if (address==null)return null;
        PickModelGHTK pickModelGHTK = new PickModelGHTK();
        String[] strings = address.split(",");
        if (strings.length>=4){
            pickModelGHTK.setPick_address(strings[0]);
            pickModelGHTK.setPick_province(strings[3]);
            pickModelGHTK.setPick_district(strings[2]);
            pickModelGHTK.setPick_ward(strings[1]);
        }else if (strings.length==3){
            pickModelGHTK.setPick_address(strings[0]);
            pickModelGHTK.setPick_province(strings[2]);
            pickModelGHTK.setPick_district(strings[1]);
            pickModelGHTK.setPick_ward("Phường Côc Lếu");
        }else {
            return null;
        }


        return pickModelGHTK;
    }
}
