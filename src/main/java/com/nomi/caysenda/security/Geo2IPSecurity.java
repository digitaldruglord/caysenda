package com.nomi.caysenda.security;

import com.maxmind.geoip2.DatabaseReader;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.record.Country;
import com.nomi.caysenda.exceptions.authentication.Geo2IPException;
import com.nomi.caysenda.options.model.Geo2IP;
import com.nomi.caysenda.options.model.GeoCountry;
import com.nomi.caysenda.services.SettingService;
import com.nomi.caysenda.utils.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class Geo2IPSecurity {
    @Autowired
    SettingService settingService;
    public void run(HttpServletRequest request) throws IOException, Geo2IPException, GeoIp2Exception {
        Geo2IP geo2IP = settingService.getGeo2IP();
        if (geo2IP.getEnable()){
            File s = UploadFileUtils.getPath("geo2ip/");
            File database = new File(s.getPath()+"/GeoLite2-Country.mmdb");
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            String ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
            if (!ip.equals("0:0:0:0:0:0:0:1")){
                InetAddress ipAddress = InetAddress.getByName(ip);
                Country country = reader.country(ipAddress).getCountry();
                if (checkExists(geo2IP.getList(),country.getIsoCode())){
                    throw new Geo2IPException();
                }
            }

        }

    }
    private Boolean checkExists(List<GeoCountry> list,String code){
        if (!code.equals("VN")){
            return true;
        }
//       for (GeoCountry geoCountry:list){
//           if (geoCountry.getValue().equals(code)){
//               return true;
//           }
//       }
       return false;
    }
}
