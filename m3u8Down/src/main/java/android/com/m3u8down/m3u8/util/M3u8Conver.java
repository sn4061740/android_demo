package android.com.m3u8down.m3u8.util;

import android.com.m3u8down.m3u8.M3U8;
import android.com.m3u8down.m3u8.M3U8Ts;
import android.com.m3u8down.utils.AESUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class M3u8Conver {

    public static M3U8 getM3u8(File file,String name,String appid) throws IOException {
        if(!file.exists()){
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        InputStream in = AESUtils.getInputStream(inputStream, appid.substring(0, 16), appid.substring(16));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        M3U8 ret = new M3U8();
        ret.setBasepath(name);
        String line;
        float seconds = 0;
        while ((line = reader.readLine()) != null) {//还要获取 key
            if (line.startsWith("#")) {
                if (line.startsWith("#EXTINF:")) {
                    line = line.substring(8);
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    seconds = Float.parseFloat(line);
                } else if (line.startsWith("#EXT-X-KEY:")) {//有key
                    int startIndex = line.indexOf("URI=");
                    int endIndex = line.indexOf(",IV=");
                    line = line.substring(startIndex + "URI=".length() + 1, endIndex - 1);
                    ret.addTs(new M3U8Ts(line, 0));
                }
                continue;
            }
            ret.addTs(new M3U8Ts(line, seconds));
            seconds = 0;
        }
        if (reader != null) {
            reader.close();
        }
        return  ret;
    }

}
