package cn.treeh.ToNX.util;


import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Tobin on 2017/8/4.
 */
public final class CodecUtil {
    public static String encodeURL(String source){
        String target;
        try{
            target = URLEncoder.encode(source,"UTF-8");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return target;
    }
    public static String decodeURL(String source){
        String str;
        try{
            str = URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return str;
    }
}
