package cn.treeh.ToNX.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Tobin on 2017/8/3.
 */
public class StringUtil {
    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    public static String[] splitString(String str,String regex){
        return str.split(regex);
    }
    public static String paste(String[] sp, String collapse){
        if(sp.length < 1)
            return "";
        StringBuilder builder = new StringBuilder(sp[0]);
        for(int index = 1; index < sp.length; index++){
            builder.append(collapse).append(sp[index]);
        }
        return builder.toString();
    }
}
