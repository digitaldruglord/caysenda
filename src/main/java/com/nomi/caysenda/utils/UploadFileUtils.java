package com.nomi.caysenda.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class UploadFileUtils {
    public static File getPath(String folder){
        File file = null;
        String os = System.getProperty("os.name");
        if (os.equals("Windows 10")){
            try {
                URL url = ResourceUtils.getURL("classpath:"+folder);
                return new File(url.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            file = new File("/opt/tomcat/cayxanh/ROOT/WEB-INF/classes/"+folder);
        }

        return file;
    }
}
